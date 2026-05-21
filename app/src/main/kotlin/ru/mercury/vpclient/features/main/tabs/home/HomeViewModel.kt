package ru.mercury.vpclient.features.main.tabs.home

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.cart.navigation.CartRoute
import ru.mercury.vpclient.features.main.tabs.home.intent.HomeIntent
import ru.mercury.vpclient.features.main.tabs.home.model.HomeModel
import ru.mercury.vpclient.shared.domain.interactor.Interactor
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val interactor: Interactor
): ClientViewModel<HomeIntent, HomeModel, Event>(HomeModel()) {

    init {
        dispatch(HomeIntent.CollectCartSize)
        dispatch(HomeIntent.CollectActiveEmployee)
        dispatch(HomeIntent.LoadCartData)
    }

    override fun dispatch(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.CollectCartSize -> {
                launch {
                    interactor.cartSize
                        .distinctUntilChanged()
                        .collectLatest { size ->
                            reduce { it.copy(cartSize = size) }
                        }
                }
            }
            is HomeIntent.CollectActiveEmployee -> {
                launch {
                    interactor.employeeEntitiesFlow
                        .map { employees -> employees.firstOrNull { it.isActive }?.employeeId.orEmpty() }
                        .distinctUntilChanged()
                        .collectLatest { employeeId ->
                            if (employeeId.isNotEmpty()) {
                                dispatch(HomeIntent.LoadCartData)
                            }
                        }
                }
            }
            is HomeIntent.LoadCartData -> {
                launch {
                    runCatching { interactor.loadBasket() }

                    val badge = runCatching { interactor.cartBadge() }.getOrDefault(0)
                    reduce { it.copy(cartBadge = badge) }
                }
            }
            is HomeIntent.CartClick -> launch { MainEventManager.send(CartRoute) }
        }
    }
}
