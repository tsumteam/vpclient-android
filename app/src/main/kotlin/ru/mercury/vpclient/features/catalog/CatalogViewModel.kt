package ru.mercury.vpclient.features.catalog

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.cart.navigation.CartPage
import ru.mercury.vpclient.features.cart.navigation.CartRoute
import ru.mercury.vpclient.features.catalog.event.CatalogEvent
import ru.mercury.vpclient.features.catalog.intent.CatalogIntent
import ru.mercury.vpclient.features.catalog.model.CatalogModel
import ru.mercury.vpclient.features.catalog_root.event.CatalogRootEventManager
import ru.mercury.vpclient.features.category.navigation.CategoryRoute
import ru.mercury.vpclient.features.filter.navigation.FilterRoute
import ru.mercury.vpclient.features.gift_card.navigation.GiftCardRoute
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.type.CatalogCategoryType
import ru.mercury.vpclient.shared.data.network.type.CatalogViewType
import ru.mercury.vpclient.shared.data.persistence.database.RoomException
import ru.mercury.vpclient.shared.data.persistence.database.RoomSQLiteException
import ru.mercury.vpclient.shared.domain.usecase.CartCountFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.CatalogCategoriesTopUseCase
import ru.mercury.vpclient.shared.domain.usecase.CatalogDataFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.EmployeeActiveFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.FittingCountFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.SetLastCatalogRootIdUseCase
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val catalogCategoriesTopUseCase: CatalogCategoriesTopUseCase,
    private val catalogDataFlowUseCase: CatalogDataFlowUseCase,
    private val cartCountFlowUseCase: CartCountFlowUseCase,
    private val fittingCountFlowUseCase: FittingCountFlowUseCase,
    private val employeeActiveFlowUseCase: EmployeeActiveFlowUseCase,
    private val setLastCatalogRootIdUseCase: SetLastCatalogRootIdUseCase
): ClientViewModel<CatalogIntent, CatalogModel, CatalogEvent>(CatalogModel()) {

    init {
        dispatch(CatalogIntent.CollectCatalogData)
        dispatch(CatalogIntent.CollectCartCount)
        dispatch(CatalogIntent.CollectFittingCount)
        dispatch(CatalogIntent.CollectActiveEmployee)
        dispatch(CatalogIntent.LoadCatalogCategoriesTop)
    }

    override fun dispatch(intent: CatalogIntent) {
        when (intent) {
            is CatalogIntent.CollectCatalogData -> {
                launch {
                    catalogDataFlowUseCase(Unit).collectLatest { data ->
                        reduce { it.copy(catalogData = data) }
                    }
                }
            }
            is CatalogIntent.CollectCartCount -> {
                launch {
                    cartCountFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { count -> reduce { it.copy(cartCount = count) } }
                }
            }
            is CatalogIntent.CollectFittingCount -> {
                launch {
                    fittingCountFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { count -> reduce { it.copy(fittingCount = count) } }
                }
            }
            is CatalogIntent.CollectActiveEmployee -> {
                launch {
                    employeeActiveFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { employee -> reduce { it.copy(activeEmployee = employee) } }
                }
            }
            is CatalogIntent.LoadCatalogCategoriesTop -> {
                launch { catalogCategoriesTopUseCase(Unit).getOrThrow() }
            }
            is CatalogIntent.SelectTab -> {
                val rootId = stateFlow.value.catalogData.tabs.getOrNull(intent.tabIndex)?.rootId ?: return
                launch { setLastCatalogRootIdUseCase(rootId).getOrThrow() }
            }
            is CatalogIntent.CategoryClick -> {
                val entity = intent.entity
                when (entity.categoryType) {
                    CatalogCategoryType.GIFT_CARD -> launch { MainEventManager.send(GiftCardRoute) }
                    CatalogCategoryType.ACTION -> {
                        val route = FilterRoute(
                            categoryId = entity.rootId,
                            titleCategoryId = entity.rootId,
                            subtitleCategoryId = entity.id,
                            viewTypeOverride = CatalogViewType.CATALOG_LEVEL_3,
                            actionId = entity.id
                        )
                        launch { CatalogRootEventManager.send(route) }
                    }
                    else -> {
                        launch { CatalogRootEventManager.send(CategoryRoute(entity.id)) }
                    }
                }
            }
            is CatalogIntent.CartClick -> launch { MainEventManager.send(CartRoute()) }
            is CatalogIntent.FittingClick -> {
                launch { MainEventManager.send(CartRoute(CartPage.Fitting)) }
            }
            is CatalogIntent.MessengerClick -> return
        }
    }

    override fun catch(throwable: Throwable) {
        when (throwable) {
            is CatalogCategoriesTopUseCase.CatalogCategoriesTopException -> {
                launch { send(CatalogEvent.SnackbarMessage(throwable.message)) }
            }
            is ClientException -> launch { send(CatalogEvent.SnackbarMessage(throwable.message)) }
            is RoomException, is RoomSQLiteException -> {
                launch { send(CatalogEvent.SnackbarMessage(throwable.message.orEmpty())) }
            }
            else -> super.catch(throwable)
        }
    }
}
