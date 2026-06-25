package ru.mercury.vpclient.features.fitting

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.cart.navigation.CartPage
import ru.mercury.vpclient.features.cart.navigation.CartRoute
import ru.mercury.vpclient.features.details.navigation.DetailsRoute
import ru.mercury.vpclient.features.fitting.intent.FittingIntent
import ru.mercury.vpclient.features.fitting.model.FittingModel
import ru.mercury.vpclient.features.fitting_confirmation.navigation.FittingConfirmationRoute
import ru.mercury.vpclient.shared.data.entity.FittingData
import ru.mercury.vpclient.shared.domain.interactor.CartInteractor
import ru.mercury.vpclient.shared.domain.mapper.isNotEmpty
import ru.mercury.vpclient.shared.domain.usecase.CartCountFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.FittingCountFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.EmployeeActiveFlowUseCase
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import javax.inject.Inject

@HiltViewModel
class FittingViewModel @Inject constructor(
    private val cartInteractor: CartInteractor,
    private val cartCountFlowUseCase: CartCountFlowUseCase,
    private val fittingCountFlowUseCase: FittingCountFlowUseCase,
    private val employeeActiveFlowUseCase: EmployeeActiveFlowUseCase
): ClientViewModel<FittingIntent, FittingModel, Event>(FittingModel()) {

    init {
        dispatch(FittingIntent.CollectCartCount)
        dispatch(FittingIntent.CollectFittingCount)
        dispatch(FittingIntent.CollectCartProducts)
        dispatch(FittingIntent.CollectActiveEmployee)
        dispatch(FittingIntent.LoadCartData)
        dispatch(FittingIntent.LoadFitting)
    }

    override fun dispatch(intent: FittingIntent) {
        when (intent) {
            is FittingIntent.CollectCartCount -> {
                launch {
                    cartCountFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { count ->
                            reduce { it.copy(cartCount = count) }
                        }
                }
            }
            is FittingIntent.CollectFittingCount -> {
                launch {
                    fittingCountFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { count ->
                            reduce { it.copy(fittingCount = count) }
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
                    employeeActiveFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { employee ->
                            reduce { it.copy(activeEmployee = employee) }
                            if (employee.isNotEmpty) {
                                dispatch(FittingIntent.LoadCartData)
                                dispatch(FittingIntent.LoadFitting)
                            }
                        }
                }
            }
            is FittingIntent.LoadCartData -> {
                launch {
                    runCatching { cartInteractor.loadBasket() }
                    runCatching { cartInteractor.loadFitting() }

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
