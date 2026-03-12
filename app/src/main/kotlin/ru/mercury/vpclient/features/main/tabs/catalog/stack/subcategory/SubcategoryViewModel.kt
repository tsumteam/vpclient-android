package ru.mercury.vpclient.features.main.tabs.catalog.stack.subcategory

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.mercury.vpclient.core.interactor.Interactor
import ru.mercury.vpclient.core.mvi.ClientViewModel
import ru.mercury.vpclient.core.mvi.Event
import ru.mercury.vpclient.core.navigation.BackRoute
import ru.mercury.vpclient.features.main.tabs.catalog.event.CatalogStackEventManager
import ru.mercury.vpclient.features.main.tabs.catalog.stack.subcategory.intent.SubcategoryIntent
import ru.mercury.vpclient.features.main.tabs.catalog.stack.subcategory.model.SubcategoryModel
import ru.mercury.vpclient.features.main.tabs.catalog.stack.subcategory.navigation.SubcategoryRoute

@HiltViewModel(assistedFactory = SubcategoryViewModel.Factory::class)
class SubcategoryViewModel @AssistedInject constructor(
    @Assisted private val route: SubcategoryRoute,
    private val interactor: Interactor
): ClientViewModel<SubcategoryIntent, SubcategoryModel, Event>(SubcategoryModel()) {

    init {
        dispatch(SubcategoryIntent.CollectSubcategoryEntity)
        dispatch(SubcategoryIntent.CollectSubcategoryPojos)
        dispatch(SubcategoryIntent.LoadCatalogCategoriesBottom)
    }

    override fun dispatch(intent: SubcategoryIntent) {
        when (intent) {
            is SubcategoryIntent.CollectSubcategoryEntity -> {
                launch {
                    interactor.catalogCategoryFlow(route.categoryId).collectLatest { entity ->
                        reduce { it.copy(entity = entity) }
                    }
                }
            }
            is SubcategoryIntent.CollectSubcategoryPojos -> {
                launch {
                    interactor.subcategoryPojosFlow(route.categoryId).collectLatest { pojos ->
                        reduce { it.copy(pojos = pojos.sortedBy { pojo -> pojo.entity.position }) }
                    }
                }
            }
            is SubcategoryIntent.LoadCatalogCategoriesBottom -> launch { interactor.loadCatalogCategoriesBottom(route.categoryId) }
            is SubcategoryIntent.BackClick -> launch { CatalogStackEventManager.send(BackRoute) }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(route: SubcategoryRoute): SubcategoryViewModel
    }
}
