package ru.mercury.vpclient.features.main.tabs.catalog.stack.category

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.cart.navigation.CartRoute
import ru.mercury.vpclient.features.main.tabs.catalog.event.CatalogStackEventManager
import ru.mercury.vpclient.features.main.tabs.catalog.stack.category.intent.CategoryIntent
import ru.mercury.vpclient.features.main.tabs.catalog.stack.category.model.CategoryModel
import ru.mercury.vpclient.features.main.tabs.catalog.stack.category.navigation.CategoryRoute
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter.navigation.FilterRoute
import ru.mercury.vpclient.shared.domain.interactor.Interactor
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import ru.mercury.vpclient.shared.navigation.BackRoute

@HiltViewModel(assistedFactory = CategoryViewModel.Factory::class)
class CategoryViewModel @AssistedInject constructor(
    @Assisted private val route: CategoryRoute,
    private val interactor: Interactor
): ClientViewModel<CategoryIntent, CategoryModel, Event>(CategoryModel()) {

    init {
        dispatch(CategoryIntent.CollectCategoryEntity)
        dispatch(CategoryIntent.CollectCategoryPojos)
        dispatch(CategoryIntent.CollectCartSize)
        dispatch(CategoryIntent.CollectActiveEmployee)
        dispatch(CategoryIntent.LoadCartData)
        dispatch(CategoryIntent.LoadCatalogCategoriesBottom)
    }

    override fun dispatch(intent: CategoryIntent) {
        when (intent) {
            is CategoryIntent.CollectCategoryEntity -> {
                launch {
                    interactor.catalogCategoryFlow(route.categoryId).collectLatest { entity ->
                        reduce { it.copy(entity = entity) }
                    }
                }
            }
            is CategoryIntent.CollectCategoryPojos -> {
                launch {
                    interactor.subcategoryPojosFlow(route.categoryId).collectLatest { pojos ->
                        reduce { it.copy(pojos = pojos.sortedBy { pojo -> pojo.entity.position }) }
                    }
                }
            }
            is CategoryIntent.CollectCartSize -> {
                launch {
                    interactor.cartSize
                        .distinctUntilChanged()
                        .collectLatest { size ->
                            reduce { it.copy(cartSize = size) }
                        }
                }
            }
            is CategoryIntent.CollectActiveEmployee -> {
                launch {
                    interactor.employeeEntitiesFlow
                        .map { employees -> employees.firstOrNull { it.isActive }?.employeeId.orEmpty() }
                        .distinctUntilChanged()
                        .collectLatest { employeeId ->
                            if (employeeId.isNotEmpty()) {
                                dispatch(CategoryIntent.LoadCartData)
                            }
                        }
                }
            }
            is CategoryIntent.LoadCartData -> {
                launch {
                    runCatching { interactor.loadBasket() }

                    val badge = runCatching { interactor.cartBadge() }.getOrDefault(0)
                    reduce { it.copy(cartBadge = badge) }
                }
            }
            is CategoryIntent.LoadCatalogCategoriesBottom -> launch { interactor.loadCatalogCategoriesBottom(route.categoryId) }
            is CategoryIntent.BackClick -> launch { CatalogStackEventManager.send(BackRoute) }
            is CategoryIntent.CartClick -> launch { MainEventManager.send(CartRoute) }
            is CategoryIntent.FilterClick -> {
                launch {
                    CatalogStackEventManager.send(
                        FilterRoute(
                            categoryId = intent.categoryId,
                            titleCategoryId = intent.titleCategoryId,
                            subtitleCategoryId = intent.subtitleCategoryId
                        )
                    )
                }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(route: CategoryRoute): CategoryViewModel
    }
}
