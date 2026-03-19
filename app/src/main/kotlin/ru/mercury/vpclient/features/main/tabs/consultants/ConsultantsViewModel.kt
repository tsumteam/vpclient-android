package ru.mercury.vpclient.features.main.tabs.consultants

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.mercury.vpclient.core.RoomException
import ru.mercury.vpclient.core.RoomSQLiteException
import ru.mercury.vpclient.core.exception.ClientException
import ru.mercury.vpclient.core.interactor.Interactor
import ru.mercury.vpclient.core.mvi.ClientViewModel
import ru.mercury.vpclient.features.consultant.navigation.ConsultantRoute
import ru.mercury.vpclient.features.main.tabs.consultants.event.ConsultantsEvents
import ru.mercury.vpclient.features.main.tabs.consultants.intent.ConsultantsIntent
import ru.mercury.vpclient.features.main.tabs.consultants.model.ConsultantsModel
import ru.mercury.vpclient.activity.event.MainEventManager
import javax.inject.Inject

@HiltViewModel
class ConsultantsViewModel @Inject constructor(
    private val interactor: Interactor
): ClientViewModel<ConsultantsIntent, ConsultantsModel, ConsultantsEvents>(ConsultantsModel()) {

    init {
        dispatch(ConsultantsIntent.CollectEmployees)
        dispatch(ConsultantsIntent.LoadConsultants)
    }

    override fun dispatch(intent: ConsultantsIntent) {
        when (intent) {
            is ConsultantsIntent.CollectEmployees -> {
                launch {
                    interactor.employeeEntitiesFlow.collectLatest { employees ->
                        reduce { it.copy(employees = employees) }
                    }
                }
            }
            is ConsultantsIntent.LoadConsultants -> {
                val job = launch { interactor.syncEmployees() }.also { launchedJob ->
                    launchedJob.invokeOnCompletion { reduce { it.copy(loadConsultantsJob = null) } }
                }
                reduce { it.copy(loadConsultantsJob = job) }
            }
            is ConsultantsIntent.SetActiveConsultant -> reduce {
                it.copy(
                    employees = it.employees.map { employee ->
                        employee.copy(isActive = employee.employeeId == intent.consultantId)
                    }
                )
            }
            is ConsultantsIntent.ConsultantClick -> launch { MainEventManager.send(ConsultantRoute(intent.consultantId)) }
        }
    }

    override fun catch(throwable: Throwable) {
        when (throwable) {
            is RoomException, is RoomSQLiteException -> launch { send(ConsultantsEvents.SnackbarMessage(throwable.message.orEmpty())) }
            is ClientException -> {
                reduce { it.copy(loadConsultantsJob = null) }
                launch { send(ConsultantsEvents.SnackbarMessage(throwable.message)) }
            }
            else -> super.catch(throwable)
        }
    }
}
