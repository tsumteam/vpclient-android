package ru.mercury.vpclient.features.profile_my_data

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.auth_welcome.navigation.WelcomeRoute
import ru.mercury.vpclient.features.cart.navigation.CartPage
import ru.mercury.vpclient.features.cart.navigation.CartRoute
import ru.mercury.vpclient.features.profile_my_data.intent.ProfileMyDataIntent
import ru.mercury.vpclient.features.profile_my_data.model.ProfileMyDataModel
import ru.mercury.vpclient.features.profile_stack.event.ProfileStackEventManager
import ru.mercury.vpclient.shared.domain.interactor.AuthenticationInteractor
import ru.mercury.vpclient.shared.domain.interactor.CartInteractor
import ru.mercury.vpclient.shared.domain.mapper.formatPhoneForDisplay
import ru.mercury.vpclient.shared.domain.mapper.isNotEmpty
import ru.mercury.vpclient.shared.domain.usecase.EmployeeActiveFlowUseCase
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import ru.mercury.vpclient.shared.navigation.BackRoute
import javax.inject.Inject

@HiltViewModel
class ProfileMyDataViewModel @Inject constructor(
    private val authenticationInteractor: AuthenticationInteractor,
    private val cartInteractor: CartInteractor,
    private val employeeActiveFlowUseCase: EmployeeActiveFlowUseCase
): ClientViewModel<ProfileMyDataIntent, ProfileMyDataModel, Event>(ProfileMyDataModel()) {

    init {
        dispatch(ProfileMyDataIntent.CollectCartSize)
        dispatch(ProfileMyDataIntent.CollectActiveEmployee)
        dispatch(ProfileMyDataIntent.LoadCartData)
        dispatch(ProfileMyDataIntent.LoadCurrentUser)
    }

    override fun dispatch(intent: ProfileMyDataIntent) {
        when (intent) {
            is ProfileMyDataIntent.CollectCartSize -> {
                launch {
                    cartInteractor.cartSize
                        .distinctUntilChanged()
                        .collectLatest { size ->
                            reduce { it.copy(cartSize = size) }
                        }
                }
            }
            is ProfileMyDataIntent.CollectActiveEmployee -> {
                launch {
                    employeeActiveFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { employee ->
                            reduce { it.copy(activeEmployee = employee) }
                            if (employee.isNotEmpty) {
                                dispatch(ProfileMyDataIntent.LoadCartData)
                            }
                        }
                }
            }
            is ProfileMyDataIntent.LoadCartData -> {
                launch {
                    runCatching { cartInteractor.loadBasket() }

                    val badge = runCatching { cartInteractor.cartBadge() }.getOrDefault(0)
                    reduce { it.copy(cartBadge = badge) }
                }
            }
            is ProfileMyDataIntent.LoadCurrentUser -> {
                launch {
                    runCatching { authenticationInteractor.currentUser() }
                        .onSuccess { user ->
                            reduce {
                                it.copy(
                                    surname = user.clientSurname.orEmpty(),
                                    name = user.clientName.orEmpty(),
                                    phone = user.clientPhone.orEmpty().let(::formatPhoneForDisplay),
                                    email = user.clientEmail.orEmpty()
                                )
                            }
                        }
                }
            }
            is ProfileMyDataIntent.BackClick -> launch { ProfileStackEventManager.send(BackRoute) }
            is ProfileMyDataIntent.CartClick -> launch { MainEventManager.send(CartRoute()) }
            is ProfileMyDataIntent.FittingClick -> {
                launch { MainEventManager.send(CartRoute(CartPage.Fitting)) }
            }
            is ProfileMyDataIntent.MessengerClick -> return
            is ProfileMyDataIntent.ShowDeleteProfileDialog -> {
                reduce { it.copy(isDeleteProfileDialogVisible = true) }
            }
            is ProfileMyDataIntent.DismissDeleteProfileDialog -> {
                reduce { it.copy(isDeleteProfileDialogVisible = false) }
            }
            is ProfileMyDataIntent.DeleteProfile -> {
                val job = launch {
                    reduce { it.copy(isDeleteProfileDialogVisible = false) }
                    authenticationInteractor.deleteProfile()
                    MainEventManager.send(WelcomeRoute)
                }.also { launchedJob ->
                    launchedJob.invokeOnCompletion { reduce { it.copy(deleteProfileJob = null) } }
                }
                reduce { it.copy(deleteProfileJob = job) }
            }
        }
    }
}
