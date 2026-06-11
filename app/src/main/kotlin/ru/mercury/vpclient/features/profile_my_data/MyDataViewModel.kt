package ru.mercury.vpclient.features.profile_my_data

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.cart.navigation.CartPage
import ru.mercury.vpclient.features.cart.navigation.CartRoute
import ru.mercury.vpclient.features.profile_my_data.intent.MyDataIntent
import ru.mercury.vpclient.features.profile_my_data.model.MyDataModel
import ru.mercury.vpclient.features.profile_stack.event.ProfileStackEventManager
import ru.mercury.vpclient.features.auth_welcome.navigation.WelcomeRoute
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.domain.interactor.Interactor
import ru.mercury.vpclient.shared.domain.mapper.formatPhoneForDisplay
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import ru.mercury.vpclient.shared.navigation.BackRoute
import javax.inject.Inject

@HiltViewModel
class MyDataViewModel @Inject constructor(
    private val interactor: Interactor
): ClientViewModel<MyDataIntent, MyDataModel, Event>(MyDataModel()) {

    init {
        dispatch(MyDataIntent.CollectCartSize)
        dispatch(MyDataIntent.CollectActiveEmployee)
        dispatch(MyDataIntent.LoadCartData)
        dispatch(MyDataIntent.LoadCurrentUser)
    }

    override fun dispatch(intent: MyDataIntent) {
        when (intent) {
            is MyDataIntent.CollectCartSize -> {
                launch {
                    interactor.cartSize
                        .distinctUntilChanged()
                        .collectLatest { size ->
                            reduce { it.copy(cartSize = size) }
                        }
                }
            }
            is MyDataIntent.CollectActiveEmployee -> {
                launch {
                    interactor.employeeEntitiesFlow
                        .map { employees -> employees.firstOrNull { it.isActive } }
                        .distinctUntilChanged()
                        .collectLatest { employee ->
                            reduce { it.copy(activeEmployee = employee ?: EmployeeEntity.Empty) }
                            if (employee != null) {
                                dispatch(MyDataIntent.LoadCartData)
                            }
                        }
                }
            }
            is MyDataIntent.LoadCartData -> {
                launch {
                    runCatching { interactor.loadBasket() }

                    val badge = runCatching { interactor.cartBadge() }.getOrDefault(0)
                    reduce { it.copy(cartBadge = badge) }
                }
            }
            is MyDataIntent.LoadCurrentUser -> {
                launch {
                    runCatching { interactor.currentUser() }
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
            is MyDataIntent.BackClick -> launch { ProfileStackEventManager.send(BackRoute) }
            is MyDataIntent.CartClick -> launch { MainEventManager.send(CartRoute()) }
            is MyDataIntent.FittingClick -> launch { MainEventManager.send(CartRoute(CartPage.Fitting)) }
            is MyDataIntent.MessengerClick -> return
            is MyDataIntent.ShowDeleteProfileDialog -> reduce { it.copy(isDeleteProfileDialogVisible = true) }
            is MyDataIntent.DismissDeleteProfileDialog -> reduce { it.copy(isDeleteProfileDialogVisible = false) }
            is MyDataIntent.DeleteProfile -> {
                val job = launch {
                    reduce { it.copy(isDeleteProfileDialogVisible = false) }
                    interactor.deleteProfile()
                    MainEventManager.send(WelcomeRoute)
                }.also { launchedJob ->
                    launchedJob.invokeOnCompletion { reduce { it.copy(deleteProfileJob = null) } }
                }
                reduce { it.copy(deleteProfileJob = job) }
            }
        }
    }
}
