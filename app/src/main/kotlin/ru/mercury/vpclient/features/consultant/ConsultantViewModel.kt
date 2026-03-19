package ru.mercury.vpclient.features.consultant

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.mercury.vpclient.core.RoomException
import ru.mercury.vpclient.core.RoomSQLiteException
import ru.mercury.vpclient.core.exception.ClientException
import ru.mercury.vpclient.core.interactor.Interactor
import ru.mercury.vpclient.core.mvi.ClientViewModel
import ru.mercury.vpclient.core.navigation.BackRoute
import ru.mercury.vpclient.features.consultant.event.ConsultantEvent
import ru.mercury.vpclient.features.consultant.intent.ConsultantIntent
import ru.mercury.vpclient.features.consultant.model.ConsultantModel
import ru.mercury.vpclient.features.consultant.navigation.ConsultantRoute
import ru.mercury.vpclient.activity.event.MainEventManager

@HiltViewModel(assistedFactory = ConsultantViewModel.Factory::class)
class ConsultantViewModel @AssistedInject constructor(
    @Assisted private val route: ConsultantRoute,
    private val interactor: Interactor
): ClientViewModel<ConsultantIntent, ConsultantModel, ConsultantEvent>(ConsultantModel()) {

    init {
        dispatch(ConsultantIntent.CollectConsultant)
        dispatch(ConsultantIntent.LoadConsultant)
    }

    override fun dispatch(intent: ConsultantIntent) {
        when (intent) {
            is ConsultantIntent.CollectConsultant -> {
                launch {
                    interactor.employeeEntityFlow(route.consultantId).collectLatest { entity ->
                        reduce { it.copy(employeeEntity = entity) }
                    }
                }
            }
            is ConsultantIntent.LoadConsultant -> launch { interactor.syncEmployee(route.consultantId) }
            is ConsultantIntent.BackClick -> launch { MainEventManager.send(BackRoute) }
        }
    }

    override fun catch(throwable: Throwable) {
        when (throwable) {
            is RoomException, is RoomSQLiteException -> launch { send(ConsultantEvent.SnackbarMessage(throwable.message.orEmpty())) }
            is ClientException -> launch { send(ConsultantEvent.SnackbarMessage(throwable.message)) }
            else -> super.catch(throwable)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(route: ConsultantRoute): ConsultantViewModel
    }
}
