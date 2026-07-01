package ru.mercury.vpclient.features.brands

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.brands.intent.BrandsIntent
import ru.mercury.vpclient.features.brands.model.BrandsModel
import ru.mercury.vpclient.features.cart.navigation.CartPage
import ru.mercury.vpclient.features.cart.navigation.CartRoute
import ru.mercury.vpclient.shared.domain.mapper.isNotEmpty
import ru.mercury.vpclient.shared.domain.usecase.CartBadgeUseCase
import ru.mercury.vpclient.shared.domain.usecase.CartCountFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.FittingCountFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.EmployeeActiveFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.LoadBasketUseCase
import ru.mercury.vpclient.shared.domain.usecase.LoadFittingUseCase
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import javax.inject.Inject

@HiltViewModel
class BrandsViewModel @Inject constructor(
    private val loadBasketUseCase: LoadBasketUseCase,
    private val loadFittingUseCase: LoadFittingUseCase,
    private val cartBadgeUseCase: CartBadgeUseCase,
    private val cartCountFlowUseCase: CartCountFlowUseCase,
    private val fittingCountFlowUseCase: FittingCountFlowUseCase,
    private val employeeActiveFlowUseCase: EmployeeActiveFlowUseCase
): ClientViewModel<BrandsIntent, BrandsModel, Event>(BrandsModel()) {

    init {
        dispatch(BrandsIntent.CollectCartCount)
        dispatch(BrandsIntent.CollectFittingCount)
        dispatch(BrandsIntent.CollectActiveEmployee)
        dispatch(BrandsIntent.LoadCartData)
    }

    override fun dispatch(intent: BrandsIntent) {
        when (intent) {
            is BrandsIntent.CollectCartCount -> {
                launch {
                    cartCountFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { count ->
                            reduce { it.copy(cartCount = count) }
                        }
                }
            }
            is BrandsIntent.CollectFittingCount -> {
                launch {
                    fittingCountFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { count ->
                            reduce { it.copy(fittingCount = count) }
                        }
                }
            }
            is BrandsIntent.CollectActiveEmployee -> {
                launch {
                    employeeActiveFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { employee ->
                            reduce { it.copy(activeEmployee = employee) }
                            if (employee.isNotEmpty) {
                                dispatch(BrandsIntent.LoadCartData)
                            }
                        }
                }
            }
            is BrandsIntent.LoadCartData -> {
                launch {
                    runCatching { loadBasketUseCase(Unit).getOrThrow() }
                    runCatching { loadFittingUseCase(Unit).getOrThrow() }

                    val badge = runCatching { cartBadgeUseCase(Unit).getOrThrow() }.getOrDefault(0)
                    reduce { it.copy(cartBadge = badge) }
                }
            }
            is BrandsIntent.CartClick -> launch { MainEventManager.send(CartRoute()) }
            is BrandsIntent.FittingClick -> {
                launch { MainEventManager.send(CartRoute(CartPage.Fitting)) }
            }
            is BrandsIntent.MessengerClick -> return
            is BrandsIntent.SearchClick -> return
        }
    }
}
