package ru.mercury.vpclient.features.profile

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.cart.navigation.CartPage
import ru.mercury.vpclient.features.cart.navigation.CartRoute
import ru.mercury.vpclient.features.profile.intent.ProfileIntent
import ru.mercury.vpclient.features.profile.model.ProfileModel
import ru.mercury.vpclient.features.welcome.navigation.WelcomeRoute
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.domain.interactor.Interactor
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val interactor: Interactor
): ClientViewModel<ProfileIntent, ProfileModel, Event>(ProfileModel()) {

    init {
        dispatch(ProfileIntent.CollectCartSize)
        dispatch(ProfileIntent.CollectActiveEmployee)
        dispatch(ProfileIntent.LoadEmployees)
        dispatch(ProfileIntent.LoadCartData)
    }

    override fun dispatch(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.CollectCartSize -> {
                launch {
                    interactor.cartSize
                        .distinctUntilChanged()
                        .collectLatest { size ->
                            reduce { it.copy(cartSize = size) }
                        }
                }
            }
            is ProfileIntent.CollectActiveEmployee -> {
                launch {
                    interactor.employeeEntitiesFlow
                        .map { employees -> employees.firstOrNull { it.isActive } }
                        .distinctUntilChanged()
                        .collectLatest { employee ->
                            reduce { it.copy(activeEmployee = employee ?: EmployeeEntity.Empty) }
                            if (employee != null) {
                                dispatch(ProfileIntent.LoadCartData)
                            }
                        }
                }
            }
            is ProfileIntent.LoadEmployees -> launch { runCatching { interactor.syncEmployees() } }
            is ProfileIntent.LoadCartData -> {
                launch {
                    runCatching { interactor.loadBasket() }

                    val badge = runCatching { interactor.cartBadge() }.getOrDefault(0)
                    reduce { it.copy(cartBadge = badge) }
                }
            }
            is ProfileIntent.Logout -> {
                val job = launch {
                    interactor.logout()
                    MainEventManager.send(WelcomeRoute)
                }.also { launchedJob ->
                    launchedJob.invokeOnCompletion { reduce { it.copy(logoutJob = null) } }
                }
                reduce { it.copy(logoutJob = job) }
            }
            is ProfileIntent.CartClick -> launch { MainEventManager.send(CartRoute()) }
            is ProfileIntent.FittingClick -> launch { MainEventManager.send(CartRoute(CartPage.Fitting)) }
            is ProfileIntent.MessengerClick -> return
        }
    }
}
