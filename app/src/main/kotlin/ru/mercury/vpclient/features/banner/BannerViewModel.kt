package ru.mercury.vpclient.features.banner

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.banner.event.BannerEvent
import ru.mercury.vpclient.features.banner.intent.BannerIntent
import ru.mercury.vpclient.features.banner.model.BannerModel
import ru.mercury.vpclient.features.banner.navigation.BannerRoute
import ru.mercury.vpclient.features.cart.navigation.CartPage
import ru.mercury.vpclient.features.cart.navigation.CartRoute
import ru.mercury.vpclient.features.home_root.event.HomeRootEventManager
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.persistence.database.RoomException
import ru.mercury.vpclient.shared.data.persistence.database.RoomSQLiteException
import ru.mercury.vpclient.shared.domain.usecase.CartBadgeUseCase
import ru.mercury.vpclient.shared.domain.usecase.EmployeeActiveFlowUseCase
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.navigation.BackRoute

@HiltViewModel(assistedFactory = BannerViewModel.Factory::class)
class BannerViewModel @AssistedInject constructor(
    @Assisted route: BannerRoute,
    private val cartBadgeUseCase: CartBadgeUseCase,
    private val employeeActiveFlowUseCase: EmployeeActiveFlowUseCase
): ClientViewModel<BannerIntent, BannerModel, BannerEvent>(BannerModel(url = route.url)) {

    init {
        dispatch(BannerIntent.CollectActiveEmployee)
        dispatch(BannerIntent.LoadCartData)
    }

    override fun dispatch(intent: BannerIntent) {
        when (intent) {
            is BannerIntent.CollectActiveEmployee -> {
                launch {
                    employeeActiveFlowUseCase(Unit).collectLatest { entity ->
                        reduce { it.copy(activeEmployee = entity) }
                    }
                }
            }
            is BannerIntent.LoadCartData -> {
                launch {
                    val badge = cartBadgeUseCase(Unit).getOrThrow()
                    reduce { it.copy(cartBadge = badge) }
                }
            }
            is BannerIntent.BackClick -> launch { HomeRootEventManager.send(BackRoute) }
            is BannerIntent.SearchClick -> return
            is BannerIntent.FittingClick -> {
                launch { MainEventManager.send(CartRoute(CartPage.Fitting)) }
            }
            is BannerIntent.CartClick -> launch { MainEventManager.send(CartRoute()) }
            is BannerIntent.MessengerClick -> return
        }
    }

    override fun catch(throwable: Throwable) {
        when (throwable) {
            is ClientException -> launch { send(BannerEvent.SnackbarErrorMessage(throwable.message)) }
            is RoomException, is RoomSQLiteException -> {
                launch { send(BannerEvent.SnackbarErrorMessage(throwable.message.orEmpty())) }
            }
            else -> super.catch(throwable)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(route: BannerRoute): BannerViewModel
    }
}
