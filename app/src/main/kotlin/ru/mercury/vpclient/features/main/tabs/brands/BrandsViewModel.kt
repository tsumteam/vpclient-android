package ru.mercury.vpclient.features.main.tabs.brands

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.cart.navigation.CartPage
import ru.mercury.vpclient.features.cart.navigation.CartRoute
import ru.mercury.vpclient.features.main.tabs.brands.intent.BrandsIntent
import ru.mercury.vpclient.features.main.tabs.brands.model.BrandsModel
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.domain.interactor.Interactor
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import javax.inject.Inject

@HiltViewModel
class BrandsViewModel @Inject constructor(
    private val interactor: Interactor
): ClientViewModel<BrandsIntent, BrandsModel, Event>(BrandsModel()) {

    init {
        dispatch(BrandsIntent.CollectCartSize)
        dispatch(BrandsIntent.CollectActiveEmployee)
        dispatch(BrandsIntent.LoadEmployees)
        dispatch(BrandsIntent.LoadCartData)
    }

    override fun dispatch(intent: BrandsIntent) {
        when (intent) {
            is BrandsIntent.CollectCartSize -> {
                launch {
                    interactor.cartSize
                        .distinctUntilChanged()
                        .collectLatest { size ->
                            reduce { it.copy(cartSize = size) }
                        }
                }
            }
            is BrandsIntent.CollectActiveEmployee -> {
                launch {
                    interactor.employeeEntitiesFlow
                        .map { employees -> employees.firstOrNull { it.isActive } }
                        .distinctUntilChanged()
                        .collectLatest { employee ->
                            reduce { it.copy(activeEmployee = employee ?: EmployeeEntity.Empty) }
                            if (employee != null) {
                                dispatch(BrandsIntent.LoadCartData)
                            }
                        }
                }
            }
            is BrandsIntent.LoadEmployees -> launch { runCatching { interactor.syncEmployees() } }
            is BrandsIntent.LoadCartData -> {
                launch {
                    runCatching { interactor.loadBasket() }

                    val badge = runCatching { interactor.cartBadge() }.getOrDefault(0)
                    reduce { it.copy(cartBadge = badge) }
                }
            }
            is BrandsIntent.CartClick -> launch { MainEventManager.send(CartRoute()) }
            is BrandsIntent.FittingClick -> launch { MainEventManager.send(CartRoute(CartPage.Fitting)) }
            is BrandsIntent.MessengerClick -> return
            is BrandsIntent.SearchClick -> return
        }
    }
}
