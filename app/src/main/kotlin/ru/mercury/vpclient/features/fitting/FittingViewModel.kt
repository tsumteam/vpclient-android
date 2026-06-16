package ru.mercury.vpclient.features.fitting

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.cart.navigation.CartPage
import ru.mercury.vpclient.features.cart.navigation.CartRoute
import ru.mercury.vpclient.features.details.navigation.DetailsRoute
import ru.mercury.vpclient.features.fitting_confirmation.navigation.FittingConfirmationRoute
import ru.mercury.vpclient.features.fitting.intent.FittingIntent
import ru.mercury.vpclient.features.fitting.model.FittingModel
import ru.mercury.vpclient.shared.data.entity.FittingData
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.domain.interactor.CartInteractor
import ru.mercury.vpclient.shared.domain.interactor.EmployeeInteractor
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import javax.inject.Inject

@HiltViewModel
class FittingViewModel @Inject constructor(
    private val cartInteractor: CartInteractor,
    private val employeeInteractor: EmployeeInteractor
): ClientViewModel<FittingIntent, FittingModel, Event>(FittingModel()) {

    init {
        dispatch(FittingIntent.CollectCartSize)
        dispatch(FittingIntent.CollectCartProducts)
        dispatch(FittingIntent.CollectActiveEmployee)
        dispatch(FittingIntent.LoadEmployees)
        dispatch(FittingIntent.LoadCartData)
        dispatch(FittingIntent.LoadFitting)
    }

    override fun dispatch(intent: FittingIntent) {
        when (intent) {
            is FittingIntent.CollectCartSize -> {
                launch {
                    cartInteractor.cartSize
                        .distinctUntilChanged()
                        .collectLatest { size ->
                            reduce { it.copy(cartSize = size) }
                        }
                }
            }
            is FittingIntent.CollectCartProducts -> {
                launch {
                    cartInteractor.cartProductsFlow.collectLatest { products ->
                        reduce { it.copy(products = products) }
                    }
                }
            }
            is FittingIntent.CollectActiveEmployee -> {
                launch {
                    employeeInteractor.employeeEntitiesFlow
                        .map { employees -> employees.firstOrNull { it.isActive } }
                        .distinctUntilChanged()
                        .collectLatest { employee ->
                            reduce { it.copy(activeEmployee = employee ?: EmployeeEntity.Empty) }
                            if (employee != null) {
                                dispatch(FittingIntent.LoadCartData)
                                dispatch(FittingIntent.LoadFitting)
                            }
                        }
                }
            }
            is FittingIntent.LoadEmployees -> launch { runCatching { employeeInteractor.syncEmployees() } }
            is FittingIntent.LoadCartData -> {
                launch {
                    runCatching { cartInteractor.loadBasket() }

                    val badge = runCatching { cartInteractor.cartBadge() }.getOrDefault(0)
                    reduce { it.copy(cartBadge = badge) }
                }
            }
            is FittingIntent.LoadFitting -> {
                launch {
                    val fitting = runCatching { cartInteractor.loadFitting() }.getOrDefault(FittingData())
                    reduce {
                        it.copy(
                            apiFittingProducts = fitting.products,
                            apiFittingDeliveries = fitting.deliveries
                        )
                    }
                }
            }
            is FittingIntent.CartClick -> launch { MainEventManager.send(CartRoute()) }
            is FittingIntent.FittingClick -> launch { MainEventManager.send(CartRoute(CartPage.Fitting)) }
            is FittingIntent.MessengerClick -> return
            is FittingIntent.SearchClick -> return
            is FittingIntent.BuyClick -> return
            is FittingIntent.FittingDeliveryClick -> {
                launch { MainEventManager.send(FittingConfirmationRoute(stateFlow.value.fittingProducts.map { product -> product.id })) }
            }
            is FittingIntent.ProductClick -> {
                launch { MainEventManager.send(DetailsRoute(intent.id, openedFromCart = true)) }
            }
            is FittingIntent.ChangePaySwitch -> {
                launch {
                    cartInteractor.changeFittingPaySwitch(intent.product, intent.paySwitch)
                    val fitting = runCatching { cartInteractor.loadFitting() }.getOrDefault(FittingData())
                    reduce {
                        it.copy(
                            apiFittingProducts = fitting.products,
                            apiFittingDeliveries = fitting.deliveries
                        )
                    }
                }
            }
            is FittingIntent.SelectPayMode -> reduce { it.copy(payMode = intent.mode) }
        }
    }
}
