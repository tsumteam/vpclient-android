package ru.mercury.vpclient.features.cart

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
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
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.persistence.database.RoomException
import ru.mercury.vpclient.shared.data.persistence.database.RoomSQLiteException
import ru.mercury.vpclient.shared.domain.mapper.clientFullName
import ru.mercury.vpclient.shared.domain.mapper.isFeminine
import ru.mercury.vpclient.shared.domain.mapper.moveProductAfterDrag
import ru.mercury.vpclient.shared.domain.mapper.withCenterLoading
import ru.mercury.vpclient.shared.domain.usecase.AddProductSizeUseCase.AddProductSizeException
import ru.mercury.vpclient.shared.domain.usecase.AddProductSizeUseCase
import ru.mercury.vpclient.shared.domain.usecase.BasketHideAlternativesUseCase.BasketHideAlternativesException
import ru.mercury.vpclient.shared.domain.usecase.BasketHideAlternativesUseCase
import ru.mercury.vpclient.shared.domain.usecase.BasketReturnOriginalUseCase.BasketReturnOriginalException
import ru.mercury.vpclient.shared.domain.usecase.BasketReturnOriginalUseCase
import ru.mercury.vpclient.shared.domain.usecase.BasketShowAlternativesUseCase.BasketShowAlternativesException
import ru.mercury.vpclient.shared.domain.usecase.BasketShowAlternativesUseCase
import ru.mercury.vpclient.shared.domain.usecase.CartProductsFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.ChangeFittingPaySwitchUseCase.ChangeFittingPaySwitchException
import ru.mercury.vpclient.shared.domain.usecase.ChangeFittingPaySwitchUseCase
import ru.mercury.vpclient.shared.domain.usecase.ChangePaySwitchUseCase.ChangePaySwitchException
import ru.mercury.vpclient.shared.domain.usecase.ChangePaySwitchUseCase
import ru.mercury.vpclient.shared.domain.usecase.CurrentUserUseCase
import ru.mercury.vpclient.shared.domain.usecase.DeleteLookUseCase.DeleteLookException
import ru.mercury.vpclient.shared.domain.usecase.DeleteLookUseCase
import ru.mercury.vpclient.shared.domain.usecase.DeleteProductUseCase.DeleteProductException
import ru.mercury.vpclient.shared.domain.usecase.DeleteProductUseCase
import ru.mercury.vpclient.shared.domain.usecase.DisassembleLookUseCase.DisassembleLookException
import ru.mercury.vpclient.shared.domain.usecase.DisassembleLookUseCase
import ru.mercury.vpclient.shared.domain.usecase.EmployeeActiveFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.FittingReturnProductUseCase.FittingReturnProductException
import ru.mercury.vpclient.shared.domain.usecase.FittingReturnProductUseCase
import ru.mercury.vpclient.shared.domain.usecase.FittingCountFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.LoadAvailableColorsUseCase
import ru.mercury.vpclient.shared.domain.usecase.LoadAvailableSizesUseCase
import ru.mercury.vpclient.shared.domain.usecase.LoadBasketUseCase
import ru.mercury.vpclient.shared.domain.usecase.LoadFittingUseCase
import ru.mercury.vpclient.shared.domain.usecase.LoadProductUseCase
import ru.mercury.vpclient.shared.domain.usecase.MoveProductsAfterDragUseCase.MoveProductsAfterDragException
import ru.mercury.vpclient.shared.domain.usecase.MoveProductsAfterDragUseCase
import ru.mercury.vpclient.shared.domain.usecase.ProductFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.RemoveAlternativeUseCase.RemoveAlternativeException
import ru.mercury.vpclient.shared.domain.usecase.RemoveAlternativeUseCase
import ru.mercury.vpclient.shared.domain.usecase.RemoveProductSizeUseCase.RemoveProductSizeException
import ru.mercury.vpclient.shared.domain.usecase.RemoveProductSizeUseCase
import ru.mercury.vpclient.shared.domain.usecase.SetFittingProductColorUseCase.ChangeFittingLineColorException
import ru.mercury.vpclient.shared.domain.usecase.SetFittingProductColorUseCase
import ru.mercury.vpclient.shared.domain.usecase.SetFittingProductSizeUseCase.ChangeFittingLineSizeException
import ru.mercury.vpclient.shared.domain.usecase.SetFittingProductSizeUseCase
import ru.mercury.vpclient.shared.domain.usecase.SetProductColorUseCase.SetProductColorException
import ru.mercury.vpclient.shared.domain.usecase.SetProductColorUseCase
import ru.mercury.vpclient.shared.domain.usecase.SetProductQuantityUseCase.SetProductQuantityException
import ru.mercury.vpclient.shared.domain.usecase.SetProductQuantityUseCase
import ru.mercury.vpclient.shared.domain.usecase.SetProductSizeUseCase.SetProductSizeException
import ru.mercury.vpclient.shared.domain.usecase.SetProductSizeUseCase
import ru.mercury.vpclient.shared.domain.usecase.SwitchProductWithAlternativeUseCase.SwitchProductWithAlternativeException
import ru.mercury.vpclient.shared.domain.usecase.SwitchProductWithAlternativeUseCase
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.navigation.BackRoute

private const val NO_AVAILABLE_COLORS_MESSAGE = "Для товара нет доступных цветов"

@HiltViewModel(assistedFactory = CartViewModel.Factory::class)
class CartViewModel @AssistedInject constructor(
    @Assisted private val route: CartRoute,
    private val currentUserUseCase: CurrentUserUseCase,
    private val cartProductsFlowUseCase: CartProductsFlowUseCase,
    private val loadBasketUseCase: LoadBasketUseCase,
    private val loadFittingUseCase: LoadFittingUseCase,
    private val changePaySwitchUseCase: ChangePaySwitchUseCase,
    private val changeFittingPaySwitchUseCase: ChangeFittingPaySwitchUseCase,
    private val loadAvailableSizesUseCase: LoadAvailableSizesUseCase,
    private val addProductSizeUseCase: AddProductSizeUseCase,
    private val setFittingProductSizeUseCase: SetFittingProductSizeUseCase,
    private val setProductSizeUseCase: SetProductSizeUseCase,
    private val loadAvailableColorsUseCase: LoadAvailableColorsUseCase,
    private val setFittingProductColorUseCase: SetFittingProductColorUseCase,
    private val setProductColorUseCase: SetProductColorUseCase,
    private val setProductQuantityUseCase: SetProductQuantityUseCase,
    private val switchProductWithAlternativeUseCase: SwitchProductWithAlternativeUseCase,
    private val removeAlternativeUseCase: RemoveAlternativeUseCase,
    private val basketHideAlternativesUseCase: BasketHideAlternativesUseCase,
    private val basketShowAlternativesUseCase: BasketShowAlternativesUseCase,
    private val basketReturnOriginalUseCase: BasketReturnOriginalUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val disassembleLookUseCase: DisassembleLookUseCase,
    private val deleteLookUseCase: DeleteLookUseCase,
    private val removeProductSizeUseCase: RemoveProductSizeUseCase,
    private val fittingReturnProductUseCase: FittingReturnProductUseCase,
    private val moveProductsAfterDragUseCase: MoveProductsAfterDragUseCase,
    private val employeeActiveFlowUseCase: EmployeeActiveFlowUseCase,
    private val fittingCountFlowUseCase: FittingCountFlowUseCase,
    private val productFlowUseCase: ProductFlowUseCase,
    private val loadProductUseCase: LoadProductUseCase
): ClientViewModel<CartIntent, CartModel, CartEvent>(
    CartModel(
        isCartInitialLoading = true,
        isFittingInitialLoading = true
    )
) {

    init {
        dispatch(CartIntent.CollectInitialPage)
        dispatch(CartIntent.CollectCart)
        dispatch(CartIntent.CollectFittingCount)
        dispatch(CartIntent.CollectActiveEmployee)
        dispatch(CartIntent.LoadCurrentUser)
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
                    cartProductsFlowUseCase(Unit).collectLatest { products ->
                        reduce { it.copy(products = products) }
                    }
                }
            }
            is CartIntent.CollectFittingCount -> {
                launch {
                    fittingCountFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { count ->
                            reduce { it.copy(fittingCount = count) }
                        }
                }
            }
            is CartIntent.CollectActiveEmployee -> {
                launch {
                    employeeActiveFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { employee ->
                            reduce { it.copy(activeEmployee = employee) }
                        }
                }
            }
            is CartIntent.LoadCurrentUser -> {
                launch {
                    runCatching { currentUserUseCase(Unit).getOrThrow() }
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
            is CartIntent.LoadCart -> {
                launch {
                    try {
                        loadBasketUseCase(Unit).getOrThrow()
                    } finally {
                        reduce { it.copy(isCartInitialLoading = false) }
                    }
                }
            }
            is CartIntent.LoadFitting -> {
                launch {
                    try {
                        val fitting = runCatching { loadFittingUseCase(Unit).getOrThrow() }.getOrDefault(FittingData())
                        reduce {
                            it.copy(
                                apiFittingProducts = fitting.products,
                                apiFittingDeliveries = fitting.deliveries
                            )
                        }
                    } finally {
                        reduce { it.copy(isFittingInitialLoading = false) }
                    }
                }
            }
            is CartIntent.PullToRefresh -> {
                if (stateFlow.value.isRefreshing) return
                reduce { it.copy(isRefreshing = true) }
                launch {
                    try {
                        loadBasketUseCase(Unit).getOrThrow()
                        val fitting = runCatching { loadFittingUseCase(Unit).getOrThrow() }.getOrDefault(FittingData())
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
            is CartIntent.SizeTableClick -> return
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
            is CartIntent.HideFittingProductsSheet -> {
                reduce { it.copy(isFittingProductsSheetVisible = false) }
            }
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
                                changePaySwitchUseCase(
                                    ChangePaySwitchUseCase.Params(
                                        product = intent.product,
                                        paySwitch = intent.paySwitch
                                    )
                                ).getOrThrow()
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
                    changeFittingPaySwitchUseCase(
                        ChangeFittingPaySwitchUseCase.Params(
                            product = intent.product,
                            paySwitch = intent.paySwitch
                        )
                    ).getOrThrow()
                    val fitting = runCatching { loadFittingUseCase(Unit).getOrThrow() }.getOrDefault(FittingData())
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
                    launch { loadProductUseCase(detailId).getOrThrow() }
                    productFlowUseCase(detailId).collectLatest { entity ->
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
                if (!intent.product.isSizeSelectionAvailable) {
                    return
                }
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
                    val sizes = loadAvailableSizesUseCase(intent.product).getOrThrow()
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
                val sizeId = stateFlow.value.visibleSizePickerItems.getOrNull(intent.index)?.sizeId
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
                            addSize -> addProductSizeUseCase(
                                AddProductSizeUseCase.Params(
                                    product = product,
                                    sizeId = sizeId
                                )
                            ).getOrThrow()
                            forFitting -> {
                                val confirmationRoute = stateFlow.value.fittingConfirmationRoute(product.id)
                                setFittingProductSizeUseCase(
                                    SetFittingProductSizeUseCase.Params(
                                        product = product,
                                        sizeId = sizeId
                                    )
                                ).getOrThrow()
                                when (confirmationRoute) {
                                    null -> {
                                        val fitting = runCatching { loadFittingUseCase(Unit).getOrThrow() }.getOrDefault(FittingData())
                                        reduce {
                                            it.copy(
                                                apiFittingProducts = fitting.products,
                                                apiFittingDeliveries = fitting.deliveries
                                            )
                                        }
                                    }
                                    else -> MainEventManager.send(confirmationRoute)
                                }
                            }
                            else -> setProductSizeUseCase(
                                SetProductSizeUseCase.Params(
                                    product = product,
                                    sizeId = sizeId
                                )
                            ).getOrThrow()
                        }
                    }
                }
            }
            is CartIntent.ShowColorPicker -> {
                launch {
                    val colors = loadAvailableColorsUseCase(intent.product).getOrThrow()
                    if (colors.isEmpty()) {
                        send(CartEvent.SnackbarErrorMessage(NO_AVAILABLE_COLORS_MESSAGE))
                        return@launch
                    }
                    reduce {
                        it.copy(
                            fittingEditProduct = null,
                            colorPickerProduct = intent.product,
                            colorPickerColors = colors,
                            colorPickerSelectedId = colors.firstOrNull { color -> color.selected }?.id,
                            colorPickerForFitting = intent.forFitting
                        )
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
                                val confirmationRoute = stateFlow.value.fittingConfirmationRoute(product.id)
                                setFittingProductColorUseCase(
                                    SetFittingProductColorUseCase.Params(
                                        product = product,
                                        colorId = colorId
                                    )
                                ).getOrThrow()
                                when (confirmationRoute) {
                                    null -> {
                                        val fitting = runCatching { loadFittingUseCase(Unit).getOrThrow() }.getOrDefault(FittingData())
                                        reduce {
                                            it.copy(
                                                apiFittingProducts = fitting.products,
                                                apiFittingDeliveries = fitting.deliveries
                                            )
                                        }
                                    }
                                    else -> MainEventManager.send(confirmationRoute)
                                }
                            }
                            else -> setProductColorUseCase(
                                SetProductColorUseCase.Params(
                                    product = product,
                                    colorId = colorId
                                )
                            ).getOrThrow()
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
                val quantity = stateFlow.value.quantityPickerValues.firstOrNull { it.selected }?.value ?: return
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
                        setProductQuantityUseCase(
                            SetProductQuantityUseCase.Params(
                                product = product,
                                quantity = quantity
                            )
                        ).getOrThrow()
                    }
                }
            }
            is CartIntent.AlternativeClick -> {
                reduce { it.copy(selectedAlternativeId = intent.alternative.id) }
                launch {
                    try {
                        withCenterLoading {
                            switchProductWithAlternativeUseCase(intent.alternative).getOrThrow()
                        }
                    } finally {
                        reduce { it.copy(selectedAlternativeId = null) }
                    }
                }
            }
            is CartIntent.RemoveAlternativeClick -> {
                launch { removeAlternativeUseCase(intent.alternative).getOrThrow() }
            }
            is CartIntent.HideAlternativesClick -> {
                launch { basketHideAlternativesUseCase(intent.product).getOrThrow() }
            }
            is CartIntent.HideAlternativesSwipeClick -> {
                launch { basketHideAlternativesUseCase(intent.product).getOrThrow() }
            }
            is CartIntent.ShowAlternativesSwipeClick -> {
                launch { basketShowAlternativesUseCase(intent.product).getOrThrow() }
            }
            is CartIntent.ReturnOriginalSwipeClick -> {
                launch {
                    withCenterLoading {
                        basketReturnOriginalUseCase(intent.product).getOrThrow()
                    }
                }
            }
            is CartIntent.DeleteProductSwipeClick -> {
                launch {
                    withCenterLoading {
                        deleteProductUseCase(intent.product).getOrThrow()
                    }
                }
            }
            is CartIntent.DisassembleLookSwipeClick -> {
                val products = stateFlow.value.products.filter { it.lookId == intent.lookId }
                launch {
                    withCenterLoading {
                        disassembleLookUseCase(products).getOrThrow()
                    }
                }
            }
            is CartIntent.DeleteLookSwipeClick -> {
                launch {
                    withCenterLoading {
                        deleteLookUseCase(intent.lookId).getOrThrow()
                    }
                }
            }
            is CartIntent.RemoveProductSizeClick -> {
                if (intent.product.sizeItems.size <= 1) {
                    return
                }
                launch {
                    withCenterLoading {
                        removeProductSizeUseCase(
                            RemoveProductSizeUseCase.Params(
                                product = intent.product,
                                size = intent.size
                            )
                        ).getOrThrow()
                    }
                }
            }
            is CartIntent.EditProductSwipeClick -> {
                when {
                    intent.product.sizeItems.size == 2 && !intent.product.isSold -> {
                        dispatch(CartIntent.ShowQuantityPicker(intent.product))
                    }
                    else -> reduce { it.copy(editProduct = intent.product) }
                }
            }
            is CartIntent.HideEditProductSheet -> reduce { it.copy(editProduct = null) }
            is CartIntent.EditFittingProductSwipeClick -> {
                reduce { it.copy(fittingEditProduct = intent.product) }
            }
            is CartIntent.HideFittingEditProductSheet -> {
                reduce { it.copy(fittingEditProduct = null) }
            }
            is CartIntent.ReturnFittingProductToBasketSwipeClick -> {
                launch {
                    withCenterLoading {
                        fittingReturnProductUseCase(intent.product).getOrThrow()
                        loadBasketUseCase(Unit).getOrThrow()
                        val fitting = runCatching { loadFittingUseCase(Unit).getOrThrow() }.getOrDefault(FittingData())
                        reduce {
                            it.copy(
                                apiFittingProducts = fitting.products,
                                apiFittingDeliveries = fitting.deliveries
                            )
                        }
                    }
                }
            }
            is CartIntent.AddSizeClick -> {
                when {
                    intent.product.isSizeSelectionAvailable -> {
                        dispatch(CartIntent.ShowSizePicker(intent.product, addSize = true))
                    }
                    else -> return
                }
            }
            is CartIntent.ChangeColorClick -> return
            is CartIntent.ChangeQuantityClick -> {
                dispatch(CartIntent.ShowQuantityPicker(intent.product))
            }
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
                        launch { moveProductsAfterDragUseCase(movedProducts).getOrThrow() }
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

private fun CartModel.fittingConfirmationRoute(productId: String): FittingConfirmationRoute? {
    val delivery = apiFittingDeliveries.firstOrNull { delivery ->
        delivery.products.any { product -> product.id == productId }
    } ?: return null
    val deliveryId = delivery.id.takeIf { it.isNotBlank() } ?: return null

    return FittingConfirmationRoute(
        productIds = listOf(productId),
        deliveryId = deliveryId,
        fittingType = delivery.fittingType
    )
}
