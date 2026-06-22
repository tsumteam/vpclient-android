package ru.mercury.vpclient.features.cart

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.cart.event.CartEvent
import ru.mercury.vpclient.features.cart.intent.CartIntent
import ru.mercury.vpclient.features.cart.model.CartModel
import ru.mercury.vpclient.features.cart.navigation.CartPage
import ru.mercury.vpclient.features.cart.navigation.CartRoute
import ru.mercury.vpclient.features.details.navigation.DetailsRoute
import ru.mercury.vpclient.features.fitting_confirmation.navigation.FittingConfirmationRoute
import ru.mercury.vpclient.features.fitting_info.navigation.FittingInfoRoute
import ru.mercury.vpclient.shared.data.entity.FittingData
import ru.mercury.vpclient.shared.data.error.AddProductSizeException
import ru.mercury.vpclient.shared.data.error.BasketHideAlternativesException
import ru.mercury.vpclient.shared.data.error.BasketReturnOriginalException
import ru.mercury.vpclient.shared.data.error.BasketShowAlternativesException
import ru.mercury.vpclient.shared.data.error.ChangeFittingLineColorException
import ru.mercury.vpclient.shared.data.error.ChangeFittingLineSizeException
import ru.mercury.vpclient.shared.data.error.ChangeFittingPaySwitchException
import ru.mercury.vpclient.shared.data.error.ChangePaySwitchException
import ru.mercury.vpclient.shared.data.error.ClientException
import ru.mercury.vpclient.shared.data.error.DeleteLookException
import ru.mercury.vpclient.shared.data.error.DeleteProductException
import ru.mercury.vpclient.shared.data.error.DisassembleLookException
import ru.mercury.vpclient.shared.data.error.FittingReturnProductException
import ru.mercury.vpclient.shared.data.error.MoveProductsAfterDragException
import ru.mercury.vpclient.shared.data.error.RemoveAlternativeException
import ru.mercury.vpclient.shared.data.error.RemoveProductSizeException
import ru.mercury.vpclient.shared.data.error.SetProductColorException
import ru.mercury.vpclient.shared.data.error.SetProductQuantityException
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
import ru.mercury.vpclient.shared.domain.mapper.moveProductAfterDrag
import ru.mercury.vpclient.shared.domain.mapper.withCenterLoading
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.navigation.BackRoute

@HiltViewModel(assistedFactory = CartViewModel.Factory::class)
class CartViewModel @AssistedInject constructor(
    @Assisted private val route: CartRoute,
    private val authenticationInteractor: AuthenticationInteractor,
    private val cartInteractor: CartInteractor,
    private val employeeInteractor: EmployeeInteractor,
    private val productInteractor: ProductInteractor
): ClientViewModel<CartIntent, CartModel, CartEvent>(CartModel()) {

    init {
        dispatch(CartIntent.CollectInitialPage)
        dispatch(CartIntent.CollectCart)
        dispatch(CartIntent.CollectActiveEmployee)
        dispatch(CartIntent.LoadCurrentUser)
        dispatch(CartIntent.LoadActiveEmployee)
        dispatch(CartIntent.LoadCart)
        dispatch(CartIntent.LoadFitting)
    }

    override fun dispatch(intent: CartIntent) {
        when (intent) {
            is CartIntent.CollectInitialPage -> {
                reduce {
                    it.copy(
                        initialPage = when (route.page) {
                            CartPage.Cart -> CartModel.CART_PAGE_INDEX
                            CartPage.Fitting -> CartModel.FITTING_PAGE_INDEX
                        }
                    )
                }
            }
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
                            apiFittingDeliveries = fitting.deliveries
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
                                apiFittingDeliveries = fitting.deliveries
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
                launch {
                    when {
                        intent.header.isDelivered -> {
                            MainEventManager.send(
                                FittingInfoRoute(
                                    address = intent.header.address,
                                    deliveryDate = intent.header.date
                                )
                            )
                        }
                        else -> {
                            MainEventManager.send(
                                FittingConfirmationRoute(
                                    productIds = intent.productIds,
                                    deliveryId = intent.deliveryId,
                                    fittingType = intent.fittingType
                                )
                            )
                        }
                    }
                }
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
                        dispatch(CartIntent.ShowSizePicker(intent.product))
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
                            apiFittingDeliveries = fitting.deliveries
                        )
                    }
                }
            }
            is CartIntent.ShowSizePicker -> {
                val detailId = intent.product.detailId
                if (detailId.isEmpty()) {
                    return
                }
                stateFlow.value.sizePickerJob?.cancel()
                reduce {
                    it.copy(
                        sizePickerProduct = intent.product,
                        sizePickerSizes = null,
                        sizePickerSelectedId = null,
                        sizePickerForFitting = false,
                        sizePickerAddSize = intent.addSize,
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
            is CartIntent.ShowFittingSizePicker -> {
                stateFlow.value.sizePickerJob?.cancel()
                reduce {
                    it.copy(
                        fittingEditProduct = null,
                        sizePickerProduct = intent.product,
                        sizePickerSizes = null,
                        sizePickerSelectedId = null,
                        sizePickerForFitting = true,
                        sizePickerAddSize = false,
                        sizePickerJob = null
                    )
                }
                val sizePickerJob = launch {
                    val sizes = cartInteractor.loadAvailableSizes(intent.product)
                    reduce {
                        when (it.sizePickerProduct?.id) {
                            intent.product.id -> it.copy(sizePickerSizes = sizes)
                            else -> it
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
                        sizePickerForFitting = false,
                        sizePickerAddSize = false,
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
                val forFitting = stateFlow.value.sizePickerForFitting
                val addSize = stateFlow.value.sizePickerAddSize
                stateFlow.value.sizePickerJob?.cancel()
                reduce {
                    it.copy(
                        sizePickerProduct = null,
                        sizePickerSizes = null,
                        sizePickerSelectedId = null,
                        sizePickerForFitting = false,
                        sizePickerAddSize = false,
                        sizePickerJob = null
                    )
                }
                launch {
                    withCenterLoading {
                        when {
                            addSize -> cartInteractor.addProductSize(product, sizeId)
                            forFitting -> {
                                cartInteractor.setFittingProductSize(product, sizeId)
                                val fitting = runCatching { cartInteractor.loadFitting() }.getOrDefault(FittingData())
                                reduce {
                                    it.copy(
                                        apiFittingProducts = fitting.products,
                                        apiFittingDeliveries = fitting.deliveries
                                    )
                                }
                            }
                            else -> cartInteractor.setProductSize(product, sizeId)
                        }
                    }
                }
            }
            is CartIntent.ShowColorPicker -> {
                reduce {
                    it.copy(
                        fittingEditProduct = null,
                        colorPickerProduct = intent.product,
                        colorPickerColors = null,
                        colorPickerSelectedId = null,
                        colorPickerForFitting = intent.forFitting
                    )
                }
                launch {
                    val colors = cartInteractor.loadAvailableColors(intent.product)
                    reduce {
                        when (it.colorPickerProduct?.id) {
                            intent.product.id -> {
                                it.copy(
                                    colorPickerColors = colors,
                                    colorPickerSelectedId = colors.firstOrNull { color -> color.selected }?.id
                                )
                            }
                            else -> it
                        }
                    }
                }
            }
            is CartIntent.HideColorPicker -> {
                reduce {
                    it.copy(
                        colorPickerProduct = null,
                        colorPickerColors = null,
                        colorPickerSelectedId = null,
                        colorPickerForFitting = false
                    )
                }
            }
            is CartIntent.ToggleColorPickerItem -> {
                val colorId = stateFlow.value.colorPickerColors?.getOrNull(intent.index)?.id
                reduce { it.copy(colorPickerSelectedId = colorId) }
            }
            is CartIntent.ConfirmColorPicker -> {
                val product = stateFlow.value.colorPickerProduct ?: return
                val colorId = stateFlow.value.colorPickerSelectedId ?: return
                val forFitting = stateFlow.value.colorPickerForFitting
                if (colorId == product.colorId) {
                    dispatch(CartIntent.HideColorPicker)
                    return
                }
                reduce {
                    it.copy(
                        colorPickerProduct = null,
                        colorPickerColors = null,
                        colorPickerSelectedId = null,
                        colorPickerForFitting = false
                    )
                }
                launch {
                    withCenterLoading {
                        when {
                            forFitting -> {
                                cartInteractor.setFittingProductColor(product, colorId)
                                val fitting = runCatching { cartInteractor.loadFitting() }.getOrDefault(FittingData())
                                reduce {
                                    it.copy(
                                        apiFittingProducts = fitting.products,
                                        apiFittingDeliveries = fitting.deliveries
                                    )
                                }
                            }
                            else -> cartInteractor.setProductColor(product, colorId)
                        }
                    }
                }
            }
            is CartIntent.ShowQuantityPicker -> {
                reduce {
                    it.copy(
                        quantityPickerProduct = intent.product,
                        quantityPickerSelectedValue = intent.product.quantity
                    )
                }
            }
            is CartIntent.HideQuantityPicker -> {
                reduce {
                    it.copy(
                        quantityPickerProduct = null,
                        quantityPickerSelectedValue = null
                    )
                }
            }
            is CartIntent.ToggleQuantityPickerItem -> {
                val quantity = stateFlow.value.quantityPickerValues.getOrNull(intent.index)?.value
                reduce { it.copy(quantityPickerSelectedValue = quantity) }
            }
            is CartIntent.ConfirmQuantityPicker -> {
                val product = stateFlow.value.quantityPickerProduct ?: return
                val quantity = stateFlow.value.quantityPickerSelectedValue ?: return
                if (quantity == product.quantity) {
                    dispatch(CartIntent.HideQuantityPicker)
                    return
                }
                reduce {
                    it.copy(
                        quantityPickerProduct = null,
                        quantityPickerSelectedValue = null
                    )
                }
                launch {
                    withCenterLoading {
                        cartInteractor.setProductQuantity(product, quantity)
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
            is CartIntent.RemoveProductSizeClick -> {
                if (intent.product.sizeItems.size <= 1) {
                    return
                }
                launch {
                    withCenterLoading {
                        cartInteractor.removeProductSize(intent.product, intent.size)
                    }
                }
            }
            is CartIntent.EditProductSwipeClick -> reduce { it.copy(editProduct = intent.product) }
            is CartIntent.HideEditProductSheet -> reduce { it.copy(editProduct = null) }
            is CartIntent.EditFittingProductSwipeClick -> reduce { it.copy(fittingEditProduct = intent.product) }
            is CartIntent.HideFittingEditProductSheet -> reduce { it.copy(fittingEditProduct = null) }
            is CartIntent.ReturnFittingProductToBasketSwipeClick -> {
                launch {
                    withCenterLoading {
                        cartInteractor.fittingReturnProduct(intent.product)
                        cartInteractor.loadBasket()
                        val fitting = runCatching { cartInteractor.loadFitting() }.getOrDefault(FittingData())
                        reduce {
                            it.copy(
                                apiFittingProducts = fitting.products,
                                apiFittingDeliveries = fitting.deliveries
                            )
                        }
                    }
                }
            }
            is CartIntent.AddSizeClick -> dispatch(CartIntent.ShowSizePicker(intent.product, addSize = true))
            is CartIntent.ChangeColorClick -> return
            is CartIntent.ChangeQuantityClick -> dispatch(CartIntent.ShowQuantityPicker(intent.product))
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
            is ChangeFittingLineSizeException -> {
                launch { send(CartEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is ChangeFittingLineColorException -> {
                launch { send(CartEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is SetProductColorException -> {
                launch { send(CartEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is SetProductQuantityException -> {
                launch { send(CartEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is AddProductSizeException -> {
                launch { send(CartEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is RemoveProductSizeException -> {
                launch { send(CartEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is FittingReturnProductException -> {
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

    @AssistedFactory
    interface Factory {
        fun create(route: CartRoute): CartViewModel
    }
}
