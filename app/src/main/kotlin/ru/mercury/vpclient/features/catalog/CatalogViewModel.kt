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
import ru.mercury.vpclient.features.catalog_root.event.CatalogStackEventManager
import ru.mercury.vpclient.features.category.navigation.CategoryRoute
import ru.mercury.vpclient.shared.data.error.ClientException
import ru.mercury.vpclient.shared.data.persistence.database.RoomException
import ru.mercury.vpclient.shared.data.persistence.database.RoomSQLiteException
import ru.mercury.vpclient.shared.domain.interactor.CartInteractor
import ru.mercury.vpclient.shared.domain.interactor.CatalogInteractor
import ru.mercury.vpclient.shared.domain.mapper.isNotEmpty
import ru.mercury.vpclient.shared.domain.usecase.EmployeeActiveFlowUseCase
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val catalogInteractor: CatalogInteractor,
    private val cartInteractor: CartInteractor,
    private val employeeActiveFlowUseCase: EmployeeActiveFlowUseCase
): ClientViewModel<CatalogIntent, CatalogModel, CatalogEvent>(CatalogModel()) {

    init {
        dispatch(CatalogIntent.CollectCatalogScreenData)
        dispatch(CatalogIntent.LoadCatalogCategoriesTop)
        dispatch(CatalogIntent.CollectCartSize)
        dispatch(CatalogIntent.CollectActiveEmployee)
        dispatch(CatalogIntent.LoadCartData)
    }

    override fun dispatch(intent: CatalogIntent) {
        when (intent) {
            is CatalogIntent.CollectCatalogScreenData -> {
                launch {
                    catalogInteractor.catalogDataFlow.collectLatest { data ->
                        reduce { it.copy(catalogData = data) }
                    }
                }
            }
            is CatalogIntent.LoadCatalogCategoriesTop -> {
                launch { catalogInteractor.loadCatalogCategoriesTop() }
            }
            is CatalogIntent.CollectCartSize -> {
                launch {
                    cartInteractor.cartSize
                        .distinctUntilChanged()
                        .collectLatest { size ->
                            reduce { it.copy(cartSize = size) }
                        }
                }
            }
            is CatalogIntent.CollectActiveEmployee -> {
                launch {
                    employeeActiveFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { employee ->
                            reduce { it.copy(activeEmployee = employee) }
                            if (employee.isNotEmpty) {
                                dispatch(CatalogIntent.LoadCartData)
                            }
                        }
                }
            }
            is CatalogIntent.LoadCartData -> {
                launch {
                    runCatching { cartInteractor.loadBasket() }

                    val badge = runCatching { cartInteractor.cartBadge() }.getOrDefault(0)
                    reduce { it.copy(cartBadge = badge) }
                }
            }
            is CatalogIntent.SelectTab -> {
                val rootId = stateFlow.value.catalogData.tabs.getOrNull(intent.tabIndex)?.rootId
                if (rootId != null) {
                    launch { catalogInteractor.setLastCatalogRootId(rootId) }
                }
            }
            is CatalogIntent.CategoryClick -> {
                launch { CatalogStackEventManager.send(CategoryRoute(categoryId = intent.categoryId)) }
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
            is RoomException, is RoomSQLiteException -> {
                launch { send(CatalogEvent.SnackbarMessage(throwable.message.orEmpty())) }
            }
            is ClientException -> launch { send(CatalogEvent.SnackbarMessage(throwable.message)) }
            else -> super.catch(throwable)
        }
    }
}
