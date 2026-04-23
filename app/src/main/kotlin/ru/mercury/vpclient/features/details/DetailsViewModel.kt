package ru.mercury.vpclient.features.details

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.details.intent.DetailsIntent
import ru.mercury.vpclient.features.details.model.DetailsModel
import ru.mercury.vpclient.features.details.navigation.DetailsRoute
import ru.mercury.vpclient.features.main.tabs.catalog.event.CatalogStackEventManager
import ru.mercury.vpclient.shared.data.entity.BrandEntity
import ru.mercury.vpclient.shared.domain.interactor.Interactor
import ru.mercury.vpclient.features.mediaviewer.navigation.MediaViewerRoute
import ru.mercury.vpclient.shared.domain.mapper.BRAND_VIEW_TYPE
import ru.mercury.vpclient.shared.domain.mapper.toCatalogLinkData
import ru.mercury.vpclient.shared.domain.mapper.toFilterRoute
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import ru.mercury.vpclient.shared.navigation.BackRoute

// fixme

@HiltViewModel(assistedFactory = DetailsViewModel.Factory::class)
class DetailsViewModel @AssistedInject constructor(
    @Assisted private val route: DetailsRoute,
    private val interactor: Interactor
): ClientViewModel<DetailsIntent, DetailsModel, Event>(DetailsModel()) {

    init {
        dispatch(DetailsIntent.CollectProduct)
        dispatch(DetailsIntent.LoadProduct)
    }

    override fun dispatch(intent: DetailsIntent) {
        when (intent) {
            is DetailsIntent.CollectProduct -> launch {
                interactor.productFlow(route.id).collectLatest { productEntity ->
                    reduce { it.copy(productEntity = productEntity) }
                }
            }
            is DetailsIntent.LoadProduct -> launch { interactor.loadProduct(route.id) }
            is DetailsIntent.BackClick -> launch { CatalogStackEventManager.send(BackRoute) }
            is DetailsIntent.MessageClick -> reduce { it.copy(isMessageSheetVisible = true) }
            is DetailsIntent.SizeTableClick -> Unit
            is DetailsIntent.AddToBasketClick -> {
                val state = stateFlow.value
                if (state.selectedSizeId == null && state.isSizePickerVisible) {
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
            is DetailsIntent.ProductClick -> launch { CatalogStackEventManager.send(DetailsRoute(intent.id)) }
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

    @AssistedFactory
    interface Factory {
        fun create(route: DetailsRoute): DetailsViewModel
    }
}
