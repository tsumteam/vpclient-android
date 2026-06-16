package ru.mercury.vpclient.features.home

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.cart.navigation.CartPage
import ru.mercury.vpclient.features.cart.navigation.CartRoute
import ru.mercury.vpclient.features.home.intent.HomeIntent
import ru.mercury.vpclient.features.home.model.HomeModel
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.domain.interactor.CartInteractor
import ru.mercury.vpclient.shared.domain.interactor.EmployeeInteractor
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val cartInteractor: CartInteractor,
    private val employeeInteractor: EmployeeInteractor
): ClientViewModel<HomeIntent, HomeModel, Event>(HomeModel()) {

    init {
        dispatch(HomeIntent.CollectCartSize)
        dispatch(HomeIntent.CollectActiveEmployee)
        dispatch(HomeIntent.LoadEmployees)
        dispatch(HomeIntent.LoadCartData)
    }

    override fun dispatch(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.CollectCartSize -> {
                launch {
                    cartInteractor.cartSize
                        .distinctUntilChanged()
                        .collectLatest { size ->
                            reduce { it.copy(cartSize = size) }
                        }
                }
            }
            is HomeIntent.CollectActiveEmployee -> {
                launch {
                    employeeInteractor.employeeEntitiesFlow
                        .map { employees -> employees.firstOrNull { it.isActive } }
                        .distinctUntilChanged()
                        .collectLatest { employee ->
                            reduce { it.copy(activeEmployee = employee ?: EmployeeEntity.Empty) }
                            if (employee != null) {
                                dispatch(HomeIntent.LoadCartData)
                            }
                        }
                }
            }
            is HomeIntent.LoadEmployees -> launch { runCatching { employeeInteractor.syncEmployees() } }
            is HomeIntent.LoadCartData -> {
                launch {
                    runCatching { cartInteractor.loadBasket() }

                    val badge = runCatching { cartInteractor.cartBadge() }.getOrDefault(0)
                    reduce { it.copy(cartBadge = badge) }
                }
            }
            is HomeIntent.CartClick -> launch { MainEventManager.send(CartRoute()) }
            is HomeIntent.FittingClick -> launch { MainEventManager.send(CartRoute(CartPage.Fitting)) }
            is HomeIntent.MessengerClick -> return
            is HomeIntent.SearchClick -> return
        }
    }
}
