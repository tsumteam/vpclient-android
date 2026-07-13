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
import ru.mercury.vpclient.features.profile_root.event.ProfileRootEventManager
import ru.mercury.vpclient.shared.domain.mapper.formatPhoneForDisplay
import ru.mercury.vpclient.shared.domain.usecase.CartBadgeUseCase
import ru.mercury.vpclient.shared.domain.usecase.CartCountFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.CurrentUserUseCase
import ru.mercury.vpclient.shared.domain.usecase.DeleteProfileUseCase
import ru.mercury.vpclient.shared.domain.usecase.EmployeeActiveFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.FittingCountFlowUseCase
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import ru.mercury.vpclient.shared.navigation.BackRoute
import javax.inject.Inject

@HiltViewModel
class ProfileMyDataViewModel @Inject constructor(
    private val currentUserUseCase: CurrentUserUseCase,
    private val deleteProfileUseCase: DeleteProfileUseCase,
    private val cartBadgeUseCase: CartBadgeUseCase,
    private val cartCountFlowUseCase: CartCountFlowUseCase,
    private val fittingCountFlowUseCase: FittingCountFlowUseCase,
    private val employeeActiveFlowUseCase: EmployeeActiveFlowUseCase
): ClientViewModel<ProfileMyDataIntent, ProfileMyDataModel, Event>(ProfileMyDataModel()) {

    init {
        dispatch(ProfileMyDataIntent.CollectCartCount)
        dispatch(ProfileMyDataIntent.CollectFittingCount)
        dispatch(ProfileMyDataIntent.CollectActiveEmployee)
        dispatch(ProfileMyDataIntent.LoadCartData)
        dispatch(ProfileMyDataIntent.LoadCurrentUser)
    }

    override fun dispatch(intent: ProfileMyDataIntent) {
        when (intent) {
            is ProfileMyDataIntent.CollectCartCount -> {
                launch {
                    cartCountFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { count -> reduce { it.copy(cartCount = count) } }
                }
            }
            is ProfileMyDataIntent.CollectFittingCount -> {
                launch {
                    fittingCountFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { count -> reduce { it.copy(fittingCount = count) } }
                }
            }
            is ProfileMyDataIntent.CollectActiveEmployee -> {
                launch {
                    employeeActiveFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { employee -> reduce { it.copy(activeEmployee = employee) } }
                }
            }
            is ProfileMyDataIntent.LoadCartData -> {
                launch {
                    val badge = runCatching { cartBadgeUseCase(Unit).getOrThrow() }.getOrDefault(0)
                    reduce { it.copy(cartBadge = badge) }
                }
            }
            is ProfileMyDataIntent.LoadCurrentUser -> {
                launch {
                    runCatching { currentUserUseCase(Unit).getOrThrow() }
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
            is ProfileMyDataIntent.BackClick -> launch { ProfileRootEventManager.send(BackRoute) }
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
                    deleteProfileUseCase(Unit).getOrThrow()
                    MainEventManager.send(WelcomeRoute)
                }.also { launchedJob ->
                    launchedJob.invokeOnCompletion { reduce { it.copy(deleteProfileJob = null) } }
                }
                reduce { it.copy(deleteProfileJob = job) }
            }
        }
    }
}
