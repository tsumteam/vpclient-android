package ru.mercury.vpclient.features.details

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.cart.navigation.CartPage
import ru.mercury.vpclient.features.cart.navigation.CartRoute
import ru.mercury.vpclient.features.details.event.DetailsEvent
import ru.mercury.vpclient.features.details.intent.DetailsIntent
import ru.mercury.vpclient.features.details.model.DetailsModel
import ru.mercury.vpclient.features.details.navigation.DetailsRoute
import ru.mercury.vpclient.features.main.tabs.catalog.event.CatalogStackEventManager
import ru.mercury.vpclient.features.mediaviewer.navigation.MediaViewerRoute
import ru.mercury.vpclient.shared.data.entity.BrandEntity
import ru.mercury.vpclient.shared.data.error.AddProductToBasketException
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.domain.interactor.Interactor
import ru.mercury.vpclient.shared.domain.mapper.BRAND_VIEW_TYPE
import ru.mercury.vpclient.shared.domain.mapper.toCatalogLinkData
import ru.mercury.vpclient.shared.domain.mapper.toFilterRoute
import ru.mercury.vpclient.shared.domain.mapper.withCenterLoading
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.navigation.BackRoute

@HiltViewModel(assistedFactory = DetailsViewModel.Factory::class)
class DetailsViewModel @AssistedInject constructor(
    @Assisted private val route: DetailsRoute,
    private val interactor: Interactor
): ClientViewModel<DetailsIntent, DetailsModel, DetailsEvent>(DetailsModel()) {

    init {
        dispatch(DetailsIntent.CollectProduct)
        dispatch(DetailsIntent.CollectCartProducts)
        dispatch(DetailsIntent.CollectCartSize)
        dispatch(DetailsIntent.CollectActiveEmployee)
        dispatch(DetailsIntent.LoadEmployees)
        dispatch(DetailsIntent.LoadCartData)
        dispatch(DetailsIntent.LoadProduct)
    }

    override fun dispatch(intent: DetailsIntent) {
        when (intent) {
            is DetailsIntent.CollectProduct -> launch {
                interactor.productFlow(route.id).collectLatest { productEntity ->
                    reduce { it.copy(productEntity = productEntity) }
                }
            }
            is DetailsIntent.CollectCartProducts -> launch {
                interactor.cartProductsFlow.collectLatest { products ->
                    reduce {
                        it.copy(
                            basketProductIds = products.map { product -> product.detailId }
                                .filter(String::isNotEmpty)
                                .toSet(),
                            basketProductKeys = products
                                .filter { product -> product.itemId.isNotEmpty() && product.colorId.isNotEmpty() }
                                .map { product -> "${product.itemId}:${product.colorId}" }
                                .toSet()
                        )
                    }
                }
            }
            is DetailsIntent.CollectCartSize -> {
                launch {
                    interactor.cartSize
                        .distinctUntilChanged()
                        .collectLatest { size ->
                            reduce { it.copy(cartSize = size) }
                        }
                }
            }
            is DetailsIntent.CollectActiveEmployee -> {
                launch {
                    interactor.employeeEntitiesFlow
                        .map { employees -> employees.firstOrNull { it.isActive } }
                        .distinctUntilChanged()
                        .collectLatest { employee ->
                            reduce { it.copy(activeEmployee = employee ?: EmployeeEntity.Empty) }
                            if (employee != null) {
                                dispatch(DetailsIntent.LoadCartData)
                            }
                        }
                }
            }
            is DetailsIntent.LoadEmployees -> launch { runCatching { interactor.syncEmployees() } }
            is DetailsIntent.LoadCartData -> {
                launch {
                    runCatching { interactor.loadBasket() }

                    val badge = runCatching { interactor.cartBadge() }.getOrDefault(0)
                    reduce { it.copy(cartBadge = badge) }
                }
            }
            is DetailsIntent.LoadProduct -> launch { interactor.loadProduct(route.id) }
            is DetailsIntent.BackClick -> launch {
                when {
                    route.openedFromCart -> MainEventManager.send(BackRoute)
                    else -> CatalogStackEventManager.send(BackRoute)
                }
            }
            is DetailsIntent.CartClick -> launch { MainEventManager.send(CartRoute()) }
            is DetailsIntent.FittingClick -> launch { MainEventManager.send(CartRoute(CartPage.Fitting)) }
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
                                interactor.addProductToBasket(route.id, state.selectedSizeId)
                            }
                        }
                    }
                }
            }
            is DetailsIntent.HideSizePicker -> reduce { it.copy(isSizePickerSheetVisible = false) }
            is DetailsIntent.ShowWearWithSheet -> reduce { it.copy(isWearWithSheetVisible = true) }
            is DetailsIntent.HideWearWithSheet -> reduce { it.copy(isWearWithSheetVisible = false) }
            is DetailsIntent.ShowMessageSheet -> reduce { it.copy(isMessageSheetVisible = true) }
            is DetailsIntent.HideMessageSheet -> reduce { it.copy(isMessageSheetVisible = false) }
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
                        catalogLinkData.viewType == BRAND_VIEW_TYPE -> productEntity.categoryId
                        else -> null
                    } ?: return@launch
                    val brandEntity = when {
                        catalogLinkData.viewType == BRAND_VIEW_TYPE -> {
                            BrandEntity(
                                brand = productEntity.brand.orEmpty(),
                                urlBrandLogo = productEntity.urlBrandLogo
                            ).takeIf { entity -> entity != BrandEntity.Empty }
                        }
                        else -> null
                    }
                    val route = interactor.catalogCategory(categoryId)?.toFilterRoute(brandEntity) ?: return@launch
                    val resolvedRoute = when {
                        catalogLinkData.viewType == BRAND_VIEW_TYPE -> {
                            route.copy(
                                initialSelectedFilterValueChips = emptyList(),
                                hiddenFilterValueChipIds = catalogLinkData.selectedFilterValueChipIds,
                                viewTypeOverride = BRAND_VIEW_TYPE
                            )
                        }
                        else -> {
                            route.copy(
                                isSingleLineTitle = true,
                                initialSelectedFilterValueChips = catalogLinkData.selectedFilterValueChips
                            )
                        }
                    }
                    catalogLinkData.rootCategoryId?.let { rootCategoryId ->
                        interactor.setLastCatalogRootId(rootCategoryId)
                    }
                    CatalogStackEventManager.send(resolvedRoute)
                }
            }
            is DetailsIntent.ProductClick -> launch {
                when {
                    route.openedFromCart -> MainEventManager.send(DetailsRoute(intent.id, openedFromCart = true))
                    else -> CatalogStackEventManager.send(DetailsRoute(intent.id))
                }
            }
            is DetailsIntent.ProductBasketClick -> launch { interactor.addProductToBasket(intent.id, null) }
            is DetailsIntent.OpenMediaViewer -> launch {
                val state = stateFlow.value
                val totalCount = state.pagerImageUrls.size + if (state.selectedColorVideoUrl != null) 1 else 0
                if (totalCount > 0) {
                    MainEventManager.send(
                        MediaViewerRoute(
                            imageUrls = state.pagerImageUrls,
                            videoUrl = state.selectedColorVideoUrl,
                            initialPage = intent.initialPage.coerceIn(0, totalCount - 1)
                        )
                    )
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
