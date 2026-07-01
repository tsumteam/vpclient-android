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
import ru.mercury.vpclient.features.catalog_root.event.CatalogRootEventManager
import ru.mercury.vpclient.features.category.event.CategoryEvent
import ru.mercury.vpclient.features.category.intent.CategoryIntent
import ru.mercury.vpclient.features.category.model.CategoryModel
import ru.mercury.vpclient.features.category.navigation.CategoryRoute
import ru.mercury.vpclient.features.filter.navigation.FilterRoute
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.persistence.database.RoomException
import ru.mercury.vpclient.shared.data.persistence.database.RoomSQLiteException
import ru.mercury.vpclient.shared.domain.usecase.CartCountFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.CatalogCategoriesBottomUseCase
import ru.mercury.vpclient.shared.domain.usecase.CatalogCategoriesBottomUseCase.CatalogCategoriesBottomException
import ru.mercury.vpclient.shared.domain.usecase.CatalogCategoryFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.CatalogSubcategoryPojosFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.EmployeeActiveFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.FittingCountFlowUseCase
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.navigation.BackRoute

@HiltViewModel(assistedFactory = CategoryViewModel.Factory::class)
class CategoryViewModel @AssistedInject constructor(
    @Assisted private val route: CategoryRoute,
    private val catalogCategoryFlowUseCase: CatalogCategoryFlowUseCase,
    private val catalogCategoriesBottomUseCase: CatalogCategoriesBottomUseCase,
    private val catalogSubcategoryPojosFlowUseCase: CatalogSubcategoryPojosFlowUseCase,
    private val cartCountFlowUseCase: CartCountFlowUseCase,
    private val fittingCountFlowUseCase: FittingCountFlowUseCase,
    private val employeeActiveFlowUseCase: EmployeeActiveFlowUseCase
): ClientViewModel<CategoryIntent, CategoryModel, CategoryEvent>(CategoryModel()) {

    init {
        dispatch(CategoryIntent.CollectCategoryEntity)
        dispatch(CategoryIntent.CollectSubcategoryPojos)
        dispatch(CategoryIntent.CollectCartCount)
        dispatch(CategoryIntent.CollectFittingCount)
        dispatch(CategoryIntent.CollectActiveEmployee)
        dispatch(CategoryIntent.LoadCatalogCategoriesBottom)
    }

    override fun dispatch(intent: CategoryIntent) {
        when (intent) {
            is CategoryIntent.CollectCategoryEntity -> {
                launch {
                    catalogCategoryFlowUseCase(route.categoryId).collectLatest { entity ->
                        reduce { it.copy(catalogCategoryEntity = entity) }
                    }
                }
            }
            is CategoryIntent.CollectSubcategoryPojos -> {
                launch {
                    catalogSubcategoryPojosFlowUseCase(route.categoryId).collectLatest { pojos ->
                        reduce { it.copy(subcategoryPojos = pojos) }
                    }
                }
            }
            is CategoryIntent.CollectCartCount -> {
                launch {
                    cartCountFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { count ->
                            reduce { it.copy(cartCount = count) }
                        }
                }
            }
            is CategoryIntent.CollectFittingCount -> {
                launch {
                    fittingCountFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { count ->
                            reduce { it.copy(fittingCount = count) }
                        }
                }
            }
            is CategoryIntent.CollectActiveEmployee -> {
                launch {
                    employeeActiveFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { employee ->
                            reduce { it.copy(activeEmployee = employee) }
                        }
                }
            }
            is CategoryIntent.LoadCatalogCategoriesBottom -> {
                launch { catalogCategoriesBottomUseCase(route.categoryId).getOrThrow() }
            }
            is CategoryIntent.BackClick -> launch { CatalogRootEventManager.send(BackRoute) }
            is CategoryIntent.CartClick -> launch { MainEventManager.send(CartRoute()) }
            is CategoryIntent.FittingClick -> {
                launch { MainEventManager.send(CartRoute(CartPage.Fitting)) }
            }
            is CategoryIntent.MessengerClick -> return
            is CategoryIntent.FilterClick -> {
                val entity = intent.entity
                val currentEntity = stateFlow.value.catalogCategoryEntity
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
                    CatalogRootEventManager.send(
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

    override fun catch(throwable: Throwable) {
        when (throwable) {
            is CatalogCategoriesBottomException -> {
                launch { send(CategoryEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is ClientException -> launch { send(CategoryEvent.SnackbarErrorMessage(throwable.message)) }
            is RoomException, is RoomSQLiteException -> {
                launch { send(CategoryEvent.SnackbarErrorMessage(throwable.message.orEmpty())) }
            }
            else -> super.catch(throwable)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(route: CategoryRoute): CategoryViewModel
    }
}
