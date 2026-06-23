package ru.mercury.vpclient.features.category

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
import ru.mercury.vpclient.features.catalog_root.event.CatalogStackEventManager
import ru.mercury.vpclient.features.category.intent.CategoryIntent
import ru.mercury.vpclient.features.category.model.CategoryModel
import ru.mercury.vpclient.features.category.navigation.CategoryRoute
import ru.mercury.vpclient.features.filter.navigation.FilterRoute
import ru.mercury.vpclient.shared.domain.interactor.CartInteractor
import ru.mercury.vpclient.shared.domain.interactor.CatalogInteractor
import ru.mercury.vpclient.shared.domain.mapper.isNotEmpty
import ru.mercury.vpclient.shared.domain.usecase.EmployeeActiveFlowUseCase
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import ru.mercury.vpclient.shared.navigation.BackRoute

@HiltViewModel(assistedFactory = CategoryViewModel.Factory::class)
class CategoryViewModel @AssistedInject constructor(
    @Assisted private val route: CategoryRoute,
    private val catalogInteractor: CatalogInteractor,
    private val cartInteractor: CartInteractor,
    private val employeeActiveFlowUseCase: EmployeeActiveFlowUseCase
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
                    catalogInteractor.catalogCategoryFlow(route.categoryId).collectLatest { entity ->
                        reduce { it.copy(entity = entity) }
                    }
                }
            }
            is CategoryIntent.CollectCategoryPojos -> {
                launch {
                    catalogInteractor.subcategoryPojosFlow(route.categoryId).collectLatest { pojos ->
                        reduce { it.copy(pojos = pojos.sortedBy { pojo -> pojo.entity.position }) }
                    }
                }
            }
            is CategoryIntent.CollectCartSize -> {
                launch {
                    cartInteractor.cartSize
                        .distinctUntilChanged()
                        .collectLatest { size ->
                            reduce { it.copy(cartSize = size) }
                        }
                }
            }
            is CategoryIntent.CollectActiveEmployee -> {
                launch {
                    employeeActiveFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { employee ->
                            reduce { it.copy(activeEmployee = employee) }
                            if (employee.isNotEmpty) {
                                dispatch(CategoryIntent.LoadCartData)
                            }
                        }
                }
            }
            is CategoryIntent.LoadCartData -> {
                launch {
                    runCatching { cartInteractor.loadBasket() }

                    val badge = runCatching { cartInteractor.cartBadge() }.getOrDefault(0)
                    reduce { it.copy(cartBadge = badge) }
                }
            }
            is CategoryIntent.LoadCatalogCategoriesBottom -> {
                launch { catalogInteractor.loadCatalogCategoriesBottom(route.categoryId) }
            }
            is CategoryIntent.BackClick -> launch { CatalogStackEventManager.send(BackRoute) }
            is CategoryIntent.CartClick -> launch { MainEventManager.send(CartRoute()) }
            is CategoryIntent.FittingClick -> {
                launch { MainEventManager.send(CartRoute(CartPage.Fitting)) }
            }
            is CategoryIntent.MessengerClick -> return
            is CategoryIntent.FilterClick -> {
                val entity = intent.entity
                val currentEntity = stateFlow.value.entity
                val titleCategoryId = when {
                    entity.id == currentEntity.id -> entity.rootId
                    entity.parentId == currentEntity.id -> currentEntity.id
                    else -> entity.id
                }
                val subtitleCategoryId = when {
                    entity.id == currentEntity.id -> entity.id
                    entity.parentId == currentEntity.id -> entity.id
                    else -> entity.parentId ?: entity.id
                }
                launch {
                    CatalogStackEventManager.send(
                        FilterRoute(
                            categoryId = entity.id,
                            titleCategoryId = titleCategoryId,
                            subtitleCategoryId = subtitleCategoryId
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
