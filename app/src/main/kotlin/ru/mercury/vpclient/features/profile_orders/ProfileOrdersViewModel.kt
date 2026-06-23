package ru.mercury.vpclient.features.profile_orders

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.cart.navigation.CartPage
import ru.mercury.vpclient.features.cart.navigation.CartRoute
import ru.mercury.vpclient.features.profile_order.navigation.ProfileOrderRoute
import ru.mercury.vpclient.shared.domain.paging.ProfileOrdersPagingSource
import ru.mercury.vpclient.features.profile_orders.event.ProfileOrdersEvent
import ru.mercury.vpclient.features.profile_orders.intent.ProfileOrdersIntent
import ru.mercury.vpclient.features.profile_orders.model.ProfileOrdersModel
import ru.mercury.vpclient.features.profile_stack.event.ProfileStackEventManager
import ru.mercury.vpclient.shared.domain.interactor.AuthenticationInteractor
import ru.mercury.vpclient.shared.domain.interactor.CartInteractor
import ru.mercury.vpclient.shared.domain.interactor.OrderInteractor
import ru.mercury.vpclient.shared.domain.usecase.EmployeeActiveFlowUseCase
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.navigation.BackRoute
import javax.inject.Inject

@HiltViewModel
class ProfileOrdersViewModel @Inject constructor(
    private val authenticationInteractor: AuthenticationInteractor,
    private val cartInteractor: CartInteractor,
    private val employeeActiveFlowUseCase: EmployeeActiveFlowUseCase,
    private val orderInteractor: OrderInteractor
): ClientViewModel<ProfileOrdersIntent, ProfileOrdersModel, ProfileOrdersEvent>(ProfileOrdersModel()) {

    val ordersPagingFlow = Pager(
        config = PagingConfig(
            pageSize = PROFILE_ORDERS_LIMIT,
            initialLoadSize = PROFILE_ORDERS_LIMIT,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            ProfileOrdersPagingSource(
                authenticationInteractor = authenticationInteractor,
                orderInteractor = orderInteractor,
                pageSize = PROFILE_ORDERS_LIMIT
            )
        }
    ).flow.cachedIn(this)

    init {
        dispatch(ProfileOrdersIntent.CollectCartSize)
        dispatch(ProfileOrdersIntent.CollectActiveEmployee)
        dispatch(ProfileOrdersIntent.LoadCartData)
    }

    override fun dispatch(intent: ProfileOrdersIntent) {
        when (intent) {
            is ProfileOrdersIntent.CollectCartSize -> {
                launch {
                    cartInteractor.cartSize
                        .distinctUntilChanged()
                        .collectLatest { size ->
                            reduce { it.copy(cartSize = size) }
                        }
                }
            }
            is ProfileOrdersIntent.CollectActiveEmployee -> {
                launch {
                    employeeActiveFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { employee ->
                            reduce { it.copy(activeEmployee = employee) }
                        }
                }
            }
            is ProfileOrdersIntent.LoadCartData -> {
                launch {
                    runCatching { cartInteractor.loadBasket() }

                    val badge = runCatching { cartInteractor.cartBadge() }.getOrDefault(0)
                    reduce { it.copy(cartBadge = badge) }
                }
            }
            is ProfileOrdersIntent.PullToRefresh -> {
                if (stateFlow.value.isRefreshing) return
                reduce { it.copy(isRefreshing = true) }
                launch { send(ProfileOrdersEvent.RefreshOrders) }
                launch {
                    runCatching { cartInteractor.loadBasket() }

                    val badge = runCatching { cartInteractor.cartBadge() }.getOrDefault(0)
                    reduce { it.copy(cartBadge = badge) }
                }
            }
            is ProfileOrdersIntent.RefreshCompleted -> reduce { it.copy(isRefreshing = false) }
            is ProfileOrdersIntent.BackClick -> launch { ProfileStackEventManager.send(BackRoute) }
            is ProfileOrdersIntent.NotificationClick -> return
            is ProfileOrdersIntent.CartClick -> launch { MainEventManager.send(CartRoute()) }
            is ProfileOrdersIntent.FittingClick -> {
                launch { MainEventManager.send(CartRoute(CartPage.Fitting)) }
            }
            is ProfileOrdersIntent.MessengerClick -> return
            is ProfileOrdersIntent.OrderClick -> {
                launch {
                    ProfileStackEventManager.send(
                        ProfileOrderRoute(
                            orderNumber = intent.orderNumber,
                            amount = intent.amount
                        )
                    )
                }
            }
        }
    }

    private companion object {
        private const val PROFILE_ORDERS_LIMIT = 15
    }
}
