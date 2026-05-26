package ru.mercury.vpclient.features.cart

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.cart.event.CartEvent
import ru.mercury.vpclient.features.cart.intent.CartIntent
import ru.mercury.vpclient.features.cart.model.CartModel
import ru.mercury.vpclient.features.details.navigation.DetailsRoute
import ru.mercury.vpclient.features.fitting_confirmation.navigation.FittingConfirmationRoute
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.FittingData
import ru.mercury.vpclient.shared.data.error.BasketHideAlternativesException
import ru.mercury.vpclient.shared.data.error.BasketReturnOriginalException
import ru.mercury.vpclient.shared.data.error.BasketShowAlternativesException
import ru.mercury.vpclient.shared.data.error.ChangeFittingPaySwitchException
import ru.mercury.vpclient.shared.data.error.ChangePaySwitchException
import ru.mercury.vpclient.shared.data.error.ClientException
import ru.mercury.vpclient.shared.data.error.DeleteLookException
import ru.mercury.vpclient.shared.data.error.DeleteProductException
import ru.mercury.vpclient.shared.data.error.DisassembleLookException
import ru.mercury.vpclient.shared.data.error.MoveProductsAfterDragException
import ru.mercury.vpclient.shared.data.error.RemoveAlternativeException
import ru.mercury.vpclient.shared.data.error.SetProductSizeException
import ru.mercury.vpclient.shared.data.error.SwitchProductWithAlternativeException
import ru.mercury.vpclient.shared.data.persistence.database.RoomException
import ru.mercury.vpclient.shared.data.persistence.database.RoomSQLiteException
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.domain.interactor.AuthenticationInteractor
import ru.mercury.vpclient.shared.domain.interactor.CartInteractor
import ru.mercury.vpclient.shared.domain.interactor.EmployeeInteractor
import ru.mercury.vpclient.shared.domain.interactor.ProductInteractor
import ru.mercury.vpclient.shared.domain.mapper.clientFullName
import ru.mercury.vpclient.shared.domain.mapper.isFeminine
import ru.mercury.vpclient.shared.domain.mapper.withCenterLoading
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.navigation.BackRoute
import javax.inject.Inject

private const val SIZE_PICKER_LOAD_ERROR_MESSAGE = "Не удалось загрузить размеры"

@HiltViewModel
class CartViewModel @Inject constructor(
    private val authenticationInteractor: AuthenticationInteractor,
    private val cartInteractor: CartInteractor,
    private val employeeInteractor: EmployeeInteractor,
    private val productInteractor: ProductInteractor
): ClientViewModel<CartIntent, CartModel, CartEvent>(CartModel()) {

    init {
        dispatch(CartIntent.CollectCart)
        dispatch(CartIntent.CollectActiveEmployee)
        dispatch(CartIntent.LoadCurrentUser)
        dispatch(CartIntent.LoadActiveEmployee)
        dispatch(CartIntent.LoadCart)
        dispatch(CartIntent.LoadFitting)
    }

    override fun dispatch(intent: CartIntent) {
        when (intent) {
            is CartIntent.CollectCart -> {
                launch {
                    cartInteractor.cartProductsFlow.collectLatest { products ->
                        reduce { it.copy(products = products) }
                    }
                }
            }
            is CartIntent.CollectActiveEmployee -> {
                launch {
                    employeeInteractor.employeeEntitiesFlow
                        .map { employees -> employees.firstOrNull { it.isActive } ?: EmployeeEntity.Empty }
                        .distinctUntilChanged()
                        .collectLatest { employee ->
                            reduce { it.copy(activeEmployee = employee) }
                        }
                }
            }
            is CartIntent.LoadCurrentUser -> {
                launch {
                    runCatching { authenticationInteractor.currentUser() }
                        .onSuccess { currentUser ->
                            reduce {
                                it.copy(
                                    fittingSheetClientName = currentUser.clientFullName,
                                    isFittingSheetClientFeminine = currentUser.isFeminine
                                )
                            }
                        }
                }
            }
            is CartIntent.LoadActiveEmployee -> launch { runCatching { employeeInteractor.syncActiveEmployee() } }
            is CartIntent.LoadCart -> launch { cartInteractor.loadBasket() }
            is CartIntent.LoadFitting -> {
                launch {
                    val fitting = runCatching { cartInteractor.loadFitting() }.getOrDefault(FittingData())
                    reduce {
                        it.copy(
                            apiFittingProducts = fitting.products,
                            apiFittingDeliveryHeader = fitting.deliveryHeader
                        )
                    }
                }
            }
            is CartIntent.PullToRefresh -> {
                if (stateFlow.value.isRefreshing) return
                reduce { it.copy(isRefreshing = true) }
                launch {
                    try {
                        cartInteractor.loadBasket()
                        val fitting = runCatching { cartInteractor.loadFitting() }.getOrDefault(FittingData())
                        reduce {
                            it.copy(
                                apiFittingProducts = fitting.products,
                                apiFittingDeliveryHeader = fitting.deliveryHeader
                            )
                        }
                    } finally {
                        dispatch(CartIntent.RefreshCompleted)
                    }
                }
            }
            is CartIntent.RefreshCompleted -> reduce { it.copy(isRefreshing = false) }
            is CartIntent.CloseClick -> launch { MainEventManager.send(BackRoute) }
            is CartIntent.FittingClick -> reduce { it.copy(isFittingSheetVisible = true) }
            is CartIntent.FittingTabClick -> return
            is CartIntent.FittingDeliveryClick -> {
                launch { MainEventManager.send(FittingConfirmationRoute(stateFlow.value.apiFittingProducts.map { product -> product.id })) }
            }
            is CartIntent.HideFittingSheet -> reduce { it.copy(isFittingSheetVisible = false) }
            is CartIntent.ConfirmFittingSheet -> {
                reduce { it.copy(isFittingSheetVisible = false) }
                launch { MainEventManager.send(FittingConfirmationRoute(intent.productIds)) }
            }
            is CartIntent.ShowFittingProductsSheet -> {
                reduce {
                    it.copy(
                        isFittingSheetVisible = false,
                        isFittingProductsSheetVisible = true
                    )
                }
            }
            is CartIntent.HideFittingProductsSheet -> reduce { it.copy(isFittingProductsSheetVisible = false) }
            is CartIntent.ConfirmFittingProductsSheet -> {
                reduce { it.copy(isFittingProductsSheetVisible = false) }
                launch { MainEventManager.send(FittingConfirmationRoute(intent.productIds)) }
            }
            is CartIntent.ProductClick -> {
                launch { MainEventManager.send(DetailsRoute(intent.id, openedFromCart = true)) }
            }
            is CartIntent.ChangePaySwitch -> {
                when {
                    !intent.product.isForPayment && intent.product.size.isBlank() && intent.paySwitch -> {
                        reduce { it.copy(selectSizeProduct = intent.product) }
                    }
                    else -> {
                        stateFlow.value.paySwitchJob?.cancel()
                        val paySwitchJob = launch {
                            try {
                                cartInteractor.changePaySwitch(intent.product, intent.paySwitch)
                            } finally {
                                reduce { it.copy(paySwitchJob = null) }
                            }
                        }
                        reduce { it.copy(paySwitchJob = paySwitchJob) }
                    }
                }
            }
            is CartIntent.ChangeFittingPaySwitch -> {
                launch {
                    cartInteractor.changeFittingPaySwitch(intent.product, intent.paySwitch)
                    val fitting = runCatching { cartInteractor.loadFitting() }.getOrDefault(FittingData())
                    reduce {
                        it.copy(
                            apiFittingProducts = fitting.products,
                            apiFittingDeliveryHeader = fitting.deliveryHeader
                        )
                    }
                }
            }
            is CartIntent.SelectSizeClick -> {
                reduce { it.copy(selectSizeProduct = null) }
                dispatch(CartIntent.ShowSizePicker(intent.product))
            }
            is CartIntent.HideSelectSizeDialog -> reduce { it.copy(selectSizeProduct = null) }
            is CartIntent.ShowSizePicker -> {
                val detailId = intent.product.detailId
                if (detailId.isEmpty()) {
                    launch { send(CartEvent.SnackbarErrorMessage(SIZE_PICKER_LOAD_ERROR_MESSAGE)) }
                    return
                }
                stateFlow.value.sizePickerJob?.cancel()
                reduce {
                    it.copy(
                        sizePickerProduct = intent.product,
                        sizePickerSizes = null,
                        sizePickerSelectedId = null,
                        sizePickerJob = null
                    )
                }
                val sizePickerJob = launch {
                    launch { productInteractor.loadProduct(detailId) }
                    productInteractor.productFlow(detailId).collectLatest { entity ->
                        entity.availableSizes?.let { sizes ->
                            reduce {
                                when (it.sizePickerProduct?.detailId) {
                                    detailId -> {
                                        it.copy(sizePickerSizes = sizes)
                                    }
                                    else -> it
                                }
                            }
                        }
                    }
                }
                reduce { it.copy(sizePickerJob = sizePickerJob) }
            }
            is CartIntent.HideSizePicker -> {
                stateFlow.value.sizePickerJob?.cancel()
                reduce {
                    it.copy(
                        sizePickerProduct = null,
                        sizePickerSizes = null,
                        sizePickerSelectedId = null,
                        sizePickerJob = null
                    )
                }
            }
            is CartIntent.ToggleSizePickerItem -> {
                val sizeId = stateFlow.value.sizePickerSizes?.items?.getOrNull(intent.index)?.sizeId
                reduce { it.copy(sizePickerSelectedId = sizeId) }
            }
            is CartIntent.ConfirmSizePicker -> {
                val product = stateFlow.value.sizePickerProduct ?: return
                val sizeId = stateFlow.value.sizePickerSelectedId ?: return
                stateFlow.value.sizePickerJob?.cancel()
                reduce {
                    it.copy(
                        sizePickerProduct = null,
                        sizePickerSizes = null,
                        sizePickerSelectedId = null,
                        sizePickerJob = null
                    )
                }
                launch {
                    withCenterLoading {
                        cartInteractor.setProductSize(product, sizeId)
                    }
                }
            }
            is CartIntent.AlternativeClick -> {
                reduce { it.copy(selectedAlternativeId = intent.alternative.id) }
                launch {
                    try {
                        withCenterLoading {
                            cartInteractor.switchProductWithAlternative(intent.alternative)
                        }
                    } finally {
                        reduce { it.copy(selectedAlternativeId = null) }
                    }
                }
            }
            is CartIntent.RemoveAlternativeClick -> {
                launch { cartInteractor.removeAlternative(intent.alternative) }
            }
            is CartIntent.HideAlternativesClick -> {
                launch { cartInteractor.basketHideAlternatives(intent.product) }
            }
            is CartIntent.HideAlternativesSwipeClick -> {
                launch { cartInteractor.basketHideAlternatives(intent.product) }
            }
            is CartIntent.ShowAlternativesSwipeClick -> {
                launch { cartInteractor.basketShowAlternatives(intent.product) }
            }
            is CartIntent.ReturnOriginalSwipeClick -> {
                launch {
                    withCenterLoading {
                        cartInteractor.basketReturnOriginal(intent.product)
                    }
                }
            }
            is CartIntent.DeleteProductSwipeClick -> {
                launch {
                    withCenterLoading {
                        cartInteractor.deleteProduct(intent.product)
                    }
                }
            }
            is CartIntent.DisassembleLookSwipeClick -> {
                val products = stateFlow.value.products.filter { it.lookId == intent.lookId }
                launch {
                    withCenterLoading {
                        cartInteractor.disassembleLook(products)
                    }
                }
            }
            is CartIntent.DeleteLookSwipeClick -> {
                launch {
                    withCenterLoading {
                        cartInteractor.deleteLook(intent.lookId)
                    }
                }
            }
            is CartIntent.EditProductSwipeClick -> reduce { it.copy(editProduct = intent.product) }
            is CartIntent.HideEditProductSheet -> reduce { it.copy(editProduct = null) }
            is CartIntent.AddSizeClick,
            is CartIntent.ChangeQuantityClick,
            is CartIntent.ChangeColorClick -> return
            is CartIntent.DetachProductFromLookSwipeClick -> return
            is CartIntent.MoveProductAfterDrag -> {
                val movedProducts = stateFlow.value.products.moveProductAfterDrag(
                    productId = intent.productId,
                    targetProductId = intent.targetProductId,
                    placeAfterTarget = intent.placeAfterTarget
                )
                when (movedProducts) {
                    stateFlow.value.products -> return
                    else -> {
                        reduce { it.copy(products = movedProducts) }
                        launch { cartInteractor.moveProductsAfterDrag(movedProducts) }
                    }
                }
            }
            is CartIntent.SelectPayMode -> reduce { it.copy(payMode = intent.mode) }
            is CartIntent.ChatClick,
            is CartIntent.BuyClick -> return
        }
    }

    override fun catch(throwable: Throwable) {
        when (throwable) {
            is RoomException, is RoomSQLiteException -> {
                launch { send(CartEvent.SnackbarErrorMessage(throwable.message.orEmpty())) }
            }
            is BasketHideAlternativesException -> {
                launch { send(CartEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is BasketShowAlternativesException -> {
                launch { send(CartEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is BasketReturnOriginalException -> {
                launch { send(CartEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is DeleteProductException -> {
                launch { send(CartEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is DeleteLookException -> {
                launch { send(CartEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is DisassembleLookException -> {
                launch { send(CartEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is MoveProductsAfterDragException -> {
                launch { send(CartEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is ChangePaySwitchException -> {
                launch { send(CartEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is ChangeFittingPaySwitchException -> {
                launch { send(CartEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is RemoveAlternativeException -> {
                launch { send(CartEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is SwitchProductWithAlternativeException -> {
                launch { send(CartEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is SetProductSizeException -> {
                launch { send(CartEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is ClientException -> {
                launch { send(CartEvent.SnackbarErrorMessage(throwable.message)) }
            }
            else -> super.catch(throwable)
        }
    }
}

private fun List<CartProduct>.moveProductAfterDrag(
    productId: String,
    targetProductId: String,
    placeAfterTarget: Boolean
): List<CartProduct> {
    if (productId == targetProductId) {
        return this
    }

    val fromIndex = indexOfFirst { it.id == productId }
    val targetIndex = indexOfFirst { it.id == targetProductId }
    if (fromIndex == -1 || targetIndex == -1) {
        return this
    }

    val targetProduct = this[targetIndex]
    val product = this[fromIndex].copy(
        lookId = targetProduct.lookId,
        lookName = targetProduct.lookName,
        lookImageUrl = targetProduct.lookImageUrl
    )
    val products = toMutableList()
    products.removeAt(fromIndex)

    val actualTargetIndex = products.indexOfFirst { it.id == targetProductId }
    if (actualTargetIndex == -1) {
        return this
    }

    val insertIndex = when {
        placeAfterTarget -> actualTargetIndex + 1
        else -> actualTargetIndex
    }.coerceIn(0, products.size)
    products.add(insertIndex, product)

    return products
}
