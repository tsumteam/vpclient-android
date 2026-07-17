package ru.mercury.vpclient.features.consultant

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.cart.navigation.CartPage
import ru.mercury.vpclient.features.cart.navigation.CartRoute
import ru.mercury.vpclient.features.consultant.event.ConsultantEvent
import ru.mercury.vpclient.features.consultant.intent.ConsultantIntent
import ru.mercury.vpclient.features.consultant.model.ConsultantModel
import ru.mercury.vpclient.features.consultant.navigation.ConsultantRoute
import ru.mercury.vpclient.features.main.navigation.MainRoute
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.persistence.database.RoomException
import ru.mercury.vpclient.shared.data.persistence.database.RoomSQLiteException
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity.Companion.ID_CALL
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity.Companion.ID_CART
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity.Companion.ID_FITTING
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity.Companion.ID_SELECTION
import ru.mercury.vpclient.shared.domain.mapper.hasFittingProducts
import ru.mercury.vpclient.shared.domain.usecase.EmployeePojoFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.MyEmployeeBadgesUseCase
import ru.mercury.vpclient.shared.domain.usecase.MyEmployeeUseCase
import ru.mercury.vpclient.shared.domain.usecase.MyEmployeeUseCase.MyEmployeeException
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.navigation.BackRoute
import ru.mercury.vpclient.shared.navigation.MainTab

@HiltViewModel(assistedFactory = ConsultantViewModel.Factory::class)
class ConsultantViewModel @AssistedInject constructor(
    @Assisted private val route: ConsultantRoute,
    private val employeePojoFlowUseCase: EmployeePojoFlowUseCase,
    private val myEmployeeUseCase: MyEmployeeUseCase,
    private val myEmployeeBadgesUseCase: MyEmployeeBadgesUseCase
): ClientViewModel<ConsultantIntent, ConsultantModel, ConsultantEvent>(ConsultantModel()) {

    init {
        dispatch(ConsultantIntent.CollectEmployee)
        dispatch(ConsultantIntent.LoadEmployee)
    }

    override fun dispatch(intent: ConsultantIntent) {
        when (intent) {
            is ConsultantIntent.CollectEmployee -> {
                launch {
                    employeePojoFlowUseCase(route.consultantId).collectLatest { employee ->
                        reduce { it.copy(employeePojo = employee) }
                    }
                }
            }
            is ConsultantIntent.LoadEmployee -> {
                launch { myEmployeeUseCase(route.consultantId).getOrThrow() }
            }
            is ConsultantIntent.ActionClick -> {
                when (intent.actionId) {
                    ID_CALL -> {
                        val phone = stateFlow.value.employeePojo.entity.employeePhone.trim()
                        if (phone.isNotEmpty()) {
                            launch { send(ConsultantEvent.LaunchDialer(phone)) }
                        }
                    }
                    ID_CART -> launch { MainEventManager.send(CartRoute()) }
                    ID_FITTING -> {
                        val route = when {
                            stateFlow.value.employeePojo.hasFittingProducts -> CartRoute(CartPage.Fitting)
                            else -> CartRoute()
                        }
                        launch { MainEventManager.send(route) }
                    }
                    ID_SELECTION -> {
                        launch {
                            MainEventManager.send(
                                MainRoute(
                                    popUpToMain = true,
                                    selectedTab = MainTab.FITTING
                                )
                            )
                        }
                    }
                }
            }
            is ConsultantIntent.BackClick -> launch { MainEventManager.send(BackRoute) }
        }
    }

    override fun catch(throwable: Throwable) {
        when (throwable) {
            is RoomException, is RoomSQLiteException -> {
                launch { send(ConsultantEvent.SnackbarMessage(throwable.message.orEmpty())) }
            }
            is MyEmployeeException -> {
                launch { send(ConsultantEvent.SnackbarMessage(throwable.message)) }
            }
            is ClientException -> {
                launch { send(ConsultantEvent.SnackbarMessage(throwable.message)) }
            }
            else -> super.catch(throwable)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(route: ConsultantRoute): ConsultantViewModel
    }
}
