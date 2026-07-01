package ru.mercury.vpclient.features.consultants

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.cart.navigation.CartPage
import ru.mercury.vpclient.features.cart.navigation.CartRoute
import ru.mercury.vpclient.features.consultant.navigation.ConsultantRoute
import ru.mercury.vpclient.features.consultants.event.ConsultantsEvents
import ru.mercury.vpclient.features.consultants.intent.ConsultantsIntent
import ru.mercury.vpclient.features.consultants.model.ConsultantsModel
import ru.mercury.vpclient.features.fitting.navigation.FittingRoute
import ru.mercury.vpclient.features.main.event.MainTabsEventManager
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.persistence.database.RoomException
import ru.mercury.vpclient.shared.data.persistence.database.RoomSQLiteException
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity.Companion.ID_CALL
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity.Companion.ID_CART
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity.Companion.ID_FITTING
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity.Companion.ID_SELECTION
import ru.mercury.vpclient.shared.domain.mapper.hasFittingProducts
import ru.mercury.vpclient.shared.domain.usecase.EmployeePojosFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.MyEmployeeBadgesUseCase
import ru.mercury.vpclient.shared.domain.usecase.MyEmployeesUseCase
import ru.mercury.vpclient.shared.domain.usecase.MyEmployeesUseCase.MyEmployeesException
import ru.mercury.vpclient.shared.domain.usecase.SetActiveEmployeeUseCase
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import javax.inject.Inject

@HiltViewModel
class ConsultantsViewModel @Inject constructor(
    private val employeePojosFlowUseCase: EmployeePojosFlowUseCase,
    private val myEmployeesUseCase: MyEmployeesUseCase,
    private val myEmployeeBadgesUseCase: MyEmployeeBadgesUseCase,
    private val setActiveEmployeeUseCase: SetActiveEmployeeUseCase
): ClientViewModel<ConsultantsIntent, ConsultantsModel, ConsultantsEvents>(ConsultantsModel()) {

    init {
        dispatch(ConsultantsIntent.CollectEmployees)
        dispatch(ConsultantsIntent.LoadEmployees)
    }

    override fun dispatch(intent: ConsultantsIntent) {
        when (intent) {
            is ConsultantsIntent.CollectEmployees -> {
                launch {
                    employeePojosFlowUseCase(Unit).collectLatest { employees ->
                        reduce { it.copy(employeePojos = employees) }
                    }
                }
            }
            is ConsultantsIntent.LoadEmployees -> {
                val job = launch {
                    myEmployeesUseCase(Unit).getOrThrow()
                    myEmployeeBadgesUseCase(Unit).getOrThrow()
                }.also { launchedJob ->
                    launchedJob.invokeOnCompletion { reduce { it.copy(loadJob = null) } }
                }
                reduce { it.copy(loadJob = job) }
            }
            is ConsultantsIntent.SetActiveEmployee -> {
                launch { setActiveEmployeeUseCase(intent.employeeId).getOrThrow() }
            }
            is ConsultantsIntent.EmployeeClick -> {
                launch { MainEventManager.send(ConsultantRoute(intent.employeeId)) }
            }
            is ConsultantsIntent.EmployeeActionClick -> {
                val employee = stateFlow.value.employeePojos.firstOrNull { employee ->
                    employee.entity.employeeId == intent.employeeId
                }

                when (intent.actionId) {
                    ID_CALL -> {
                        val phone = employee?.entity?.employeePhone.orEmpty().trim()
                        if (phone.isNotEmpty()) {
                            launch { send(ConsultantsEvents.LaunchDialer(phone)) }
                        }
                    }
                    ID_CART -> launch {
                        if (employee?.entity?.isActive == false) {
                            setActiveEmployeeUseCase(intent.employeeId).getOrThrow()
                        }
                        MainEventManager.send(CartRoute())
                    }
                    ID_FITTING -> {
                        val route = when {
                            employee?.hasFittingProducts == true -> CartRoute(CartPage.Fitting)
                            else -> CartRoute()
                        }
                        launch {
                            if (employee?.entity?.isActive == false) {
                                setActiveEmployeeUseCase(intent.employeeId).getOrThrow()
                            }
                            MainEventManager.send(route)
                        }
                    }
                    ID_SELECTION -> launch {
                        if (employee?.entity?.isActive == false) {
                            setActiveEmployeeUseCase(intent.employeeId).getOrThrow()
                        }
                        MainTabsEventManager.send(FittingRoute)
                    }
                }
            }
        }
    }

    override fun catch(throwable: Throwable) {
        when (throwable) {
            is RoomException, is RoomSQLiteException -> {
                launch { send(ConsultantsEvents.SnackbarMessage(throwable.message.orEmpty())) }
            }
            is MyEmployeesException -> {
                reduce { it.copy(loadJob = null) }
                launch { send(ConsultantsEvents.SnackbarMessage(throwable.message)) }
            }
            is ClientException -> {
                reduce { it.copy(loadJob = null) }
                launch { send(ConsultantsEvents.SnackbarMessage(throwable.message)) }
            }
            else -> super.catch(throwable)
        }
    }
}
