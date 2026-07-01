package ru.mercury.vpclient.features.details

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.cart.navigation.CartPage
import ru.mercury.vpclient.features.cart.navigation.CartRoute
import ru.mercury.vpclient.features.catalog_root.event.CatalogRootEventManager
import ru.mercury.vpclient.features.details.event.DetailsEvent
import ru.mercury.vpclient.features.details.intent.DetailsIntent
import ru.mercury.vpclient.features.details.model.DetailsModel
import ru.mercury.vpclient.features.details.navigation.DetailsRoute
import ru.mercury.vpclient.features.filter.navigation.FilterRoute
import ru.mercury.vpclient.features.media.navigation.MediaRoute
import ru.mercury.vpclient.features.video.navigation.VideoRoute
import ru.mercury.vpclient.shared.data.entity.BrandEntity
import ru.mercury.vpclient.shared.data.entity.DetailsField
import ru.mercury.vpclient.shared.data.network.type.CatalogViewType
import ru.mercury.vpclient.shared.domain.mapper.filterRoute
import ru.mercury.vpclient.shared.domain.mapper.isNotEmpty
import ru.mercury.vpclient.shared.domain.mapper.toCatalogLinkData
import ru.mercury.vpclient.shared.domain.mapper.withCenterLoading
import ru.mercury.vpclient.shared.domain.usecase.AddCatalogProductToBasketUseCase
import ru.mercury.vpclient.shared.domain.usecase.AddProductToBasketUseCase
import ru.mercury.vpclient.shared.domain.usecase.CartBadgeUseCase
import ru.mercury.vpclient.shared.domain.usecase.CartCountFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.CartProductsFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.CatalogCategoryUseCase
import ru.mercury.vpclient.shared.domain.usecase.EmployeeActiveFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.FittingCountFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.LoadBasketUseCase
import ru.mercury.vpclient.shared.domain.usecase.LoadFittingUseCase
import ru.mercury.vpclient.shared.domain.usecase.LoadProductUseCase
import ru.mercury.vpclient.shared.domain.usecase.ProductFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.SetLastCatalogRootIdUseCase
import ru.mercury.vpclient.shared.domain.usecase.AddProductToBasketUseCase.AddProductToBasketException
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.navigation.BackRoute
import ru.mercury.vpclient.shared.ui.theme.ClientStrings

@HiltViewModel(assistedFactory = DetailsViewModel.Factory::class)
class DetailsViewModel @AssistedInject constructor(
    @Assisted private val route: DetailsRoute,
    private val loadBasketUseCase: LoadBasketUseCase,
    private val loadFittingUseCase: LoadFittingUseCase,
    private val cartBadgeUseCase: CartBadgeUseCase,
    private val cartProductsFlowUseCase: CartProductsFlowUseCase,
    private val addProductToBasketUseCase: AddProductToBasketUseCase,
    private val addCatalogProductToBasketUseCase: AddCatalogProductToBasketUseCase,
    private val catalogCategoryUseCase: CatalogCategoryUseCase,
    private val cartCountFlowUseCase: CartCountFlowUseCase,
    private val fittingCountFlowUseCase: FittingCountFlowUseCase,
    private val employeeActiveFlowUseCase: EmployeeActiveFlowUseCase,
    private val setLastCatalogRootIdUseCase: SetLastCatalogRootIdUseCase,
    private val productFlowUseCase: ProductFlowUseCase,
    private val loadProductUseCase: LoadProductUseCase
): ClientViewModel<DetailsIntent, DetailsModel, DetailsEvent>(DetailsModel()) {

    init {
        dispatch(DetailsIntent.CollectProduct)
        dispatch(DetailsIntent.CollectCartProducts)
        dispatch(DetailsIntent.CollectCartCount)
        dispatch(DetailsIntent.CollectFittingCount)
        dispatch(DetailsIntent.CollectActiveEmployee)
        dispatch(DetailsIntent.LoadCartData)
        dispatch(DetailsIntent.LoadProduct)
    }

    override fun dispatch(intent: DetailsIntent) {
        when (intent) {
            is DetailsIntent.CollectProduct -> launch {
                productFlowUseCase(route.id).collectLatest { productEntity ->
                    reduce { it.copy(productEntity = productEntity) }
                }
            }
            is DetailsIntent.CollectCartProducts -> launch {
                cartProductsFlowUseCase(Unit).collectLatest { products ->
                    reduce {
                        it.copy(
                            basketProductIds = products.map { product -> product.detailId }
                                .filter(String::isNotEmpty)
                                .toSet(),
                            basketProductKeys = products
                                .filter { product -> product.itemId.isNotEmpty() && product.colorId.isNotEmpty() }
                                .map { product -> "${product.itemId}:${product.colorId}:${product.sizeId}" }
                                .toSet()
                        )
                    }
                }
            }
            is DetailsIntent.CollectCartCount -> {
                launch {
                    cartCountFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { count ->
                            reduce { it.copy(cartCount = count) }
                        }
                }
            }
            is DetailsIntent.CollectFittingCount -> {
                launch {
                    fittingCountFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { count ->
                            reduce { it.copy(fittingCount = count) }
                        }
                }
            }
            is DetailsIntent.CollectActiveEmployee -> {
                launch {
                    employeeActiveFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { employee ->
                            reduce { it.copy(activeEmployee = employee) }
                            if (employee.isNotEmpty) {
                                dispatch(DetailsIntent.LoadCartData)
                            }
                        }
                }
            }
            is DetailsIntent.LoadCartData -> {
                launch {
                    runCatching { loadBasketUseCase(Unit).getOrThrow() }
                    runCatching { loadFittingUseCase(Unit).getOrThrow() }

                    val badge = runCatching { cartBadgeUseCase(Unit).getOrThrow() }.getOrDefault(0)
                    reduce { it.copy(cartBadge = badge) }
                }
            }
            is DetailsIntent.LoadProduct -> launch { loadProductUseCase(route.id).getOrThrow() }
            is DetailsIntent.BackClick -> launch {
                when {
                    route.openedFromCart -> MainEventManager.send(BackRoute)
                    else -> CatalogRootEventManager.send(BackRoute)
                }
            }
            is DetailsIntent.CartClick -> launch { MainEventManager.send(CartRoute()) }
            is DetailsIntent.FittingClick -> {
                launch { MainEventManager.send(CartRoute(CartPage.Fitting)) }
            }
            is DetailsIntent.MessengerClick -> return
            is DetailsIntent.MessageClick -> reduce { it.copy(isMessageSheetVisible = true) }
            is DetailsIntent.SizeTableClick -> Unit
            is DetailsIntent.AddToBasketClick -> {
                val state = stateFlow.value
                when {
                    state.selectedSizeId == null && state.isSizePickerVisible -> {
                        val selectedSizeId = state.productEntity.availableSizes?.items
                            ?.firstOrNull { it.inStock }
                            ?.sizeId
                            ?: state.productEntity.availableSizes?.items?.firstOrNull()?.sizeId
                        reduce {
                            it.copy(
                                selectedSizeId = selectedSizeId,
                                isSizePickerSheetVisible = true
                            )
                        }
                    }
                    else -> {
                        reduce { it.copy(isSizePickerSheetVisible = false) }
                        launch {
                            withCenterLoading {
                                addProductToBasketUseCase(
                                    AddProductToBasketUseCase.Params(
                                        productId = route.id,
                                        sizeId = state.selectedSizeId
                                    )
                                ).getOrThrow()
                            }
                            reduce { it.copy(isCartAddedSheetVisible = true) }
                        }
                    }
                }
            }
            is DetailsIntent.HideSizePicker -> reduce { it.copy(isSizePickerSheetVisible = false) }
            is DetailsIntent.ShowWearWithSheet -> reduce { it.copy(isWearWithSheetVisible = true) }
            is DetailsIntent.HideWearWithSheet -> reduce { it.copy(isWearWithSheetVisible = false) }
            is DetailsIntent.ShowMessageSheet -> reduce { it.copy(isMessageSheetVisible = true) }
            is DetailsIntent.HideMessageSheet -> reduce { it.copy(isMessageSheetVisible = false) }
            is DetailsIntent.HideCartAddedSheet -> reduce { it.copy(isCartAddedSheetVisible = false) }
            is DetailsIntent.CartAddedSheetCartClick -> {
                reduce { it.copy(isCartAddedSheetVisible = false) }
                launch { MainEventManager.send(CartRoute()) }
            }
            is DetailsIntent.SizeClick -> {
                val size = stateFlow.value.productEntity.availableSizes?.items?.getOrNull(intent.index)
                reduce { it.copy(selectedSizeId = size?.sizeId) }
            }
            is DetailsIntent.ColorClick -> reduce { model ->
                val newIndex = if (intent.index == model.selectedOtherColorIndex) null else intent.index
                model.copy(selectedOtherColorIndex = newIndex)
            }
            is DetailsIntent.ButtonClick -> {
                launch {
                    val button = stateFlow.value.productEntity.buttons.getOrNull(intent.index) ?: return@launch
                    val productEntity = stateFlow.value.productEntity
                    val catalogLinkData = button.catalogLink?.toCatalogLinkData() ?: return@launch
                    val categoryId = when {
                        catalogLinkData.categoryId != null -> catalogLinkData.categoryId
                        catalogLinkData.viewType == CatalogViewType.BRAND -> productEntity.categoryId
                        else -> null
                    } ?: return@launch
                    val brandEntity = when {
                        catalogLinkData.viewType == CatalogViewType.BRAND -> {
                            BrandEntity(
                                brand = productEntity.brand.orEmpty(),
                                urlBrandLogo = productEntity.urlBrandLogo
                            ).takeIf { entity -> entity != BrandEntity.Empty }
                        }
                        else -> null
                    }
                    val catalogCategoryEntity = catalogCategoryUseCase(categoryId).getOrThrow()
                    if (brandEntity != null && catalogCategoryEntity != null) {
                        val filterRoute: FilterRoute = catalogCategoryEntity.filterRoute(brandEntity)
                        val resolvedRoute = when {
                            catalogLinkData.viewType == CatalogViewType.BRAND -> {
                                filterRoute.copy(
                                    initialSelectedFilterValueChips = emptyList(),
                                    hiddenFilterValueChipIds = catalogLinkData.selectedFilterValueChipIds,
                                    viewTypeOverride = CatalogViewType.BRAND
                                )
                            }
                            else -> {
                                filterRoute.copy(
                                    isSingleLineTitle = true,
                                    initialSelectedFilterValueChips = catalogLinkData.selectedFilterValueChips
                                )
                            }
                        }
                        catalogLinkData.rootCategoryId?.let { rootCategoryId ->
                            setLastCatalogRootIdUseCase(rootCategoryId).getOrThrow()
                        }
                        CatalogRootEventManager.send(resolvedRoute)
                    }
                }
            }
            is DetailsIntent.ProductClick -> launch {
                when {
                    route.openedFromCart -> MainEventManager.send(DetailsRoute(intent.id, openedFromCart = true))
                    else -> CatalogRootEventManager.send(DetailsRoute(intent.id))
                }
            }
            is DetailsIntent.ProductBasketClick -> {
                launch {
                    addCatalogProductToBasketUseCase(
                        AddCatalogProductToBasketUseCase.Params(
                            product = intent.product,
                            sizeId = null
                        )
                    ).getOrThrow()
                }
            }
            is DetailsIntent.OpenVideo -> launch {
                val videoUrl = stateFlow.value.selectedColorVideoUrl ?: return@launch
                MainEventManager.send(VideoRoute(videoUrl))
            }
            is DetailsIntent.OpenMedia -> launch {
                val state = stateFlow.value
                val totalCount = state.pagerImageUrls.size + if (state.selectedColorVideoUrl != null) 1 else 0
                if (totalCount > 0) {
                    MainEventManager.send(
                        MediaRoute(
                            imageUrls = state.pagerImageUrls,
                            videoUrl = state.selectedColorVideoUrl,
                            initialPage = intent.initialPage.coerceIn(0, totalCount - 1)
                        )
                    )
                }
            }
            is DetailsIntent.FieldCopyClick -> {
                when (intent.field) {
                    is DetailsField.ItemId -> launch {
                        send(DetailsEvent.SnackbarMessage(ClientStrings.DetailsArticleCopied))
                    }
                    is DetailsField.Article -> launch {
                        send(DetailsEvent.SnackbarMessage(ClientStrings.DetailsManufacturerArticleCopied))
                    }
                    else -> Unit
                }
            }
        }
    }

    override fun catch(throwable: Throwable) {
        when (throwable) {
            is AddProductToBasketException -> {
                launch { send(DetailsEvent.SnackbarErrorMessage(throwable.message)) }
            }
            else -> super.catch(throwable)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(route: DetailsRoute): DetailsViewModel
    }
}
