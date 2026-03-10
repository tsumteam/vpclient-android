package ru.mercury.vpclient.features.consultant

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mercury.vpclient.core.mvi.ClientViewModel
import ru.mercury.vpclient.core.mvi.Event
import ru.mercury.vpclient.core.navigation.BackRoute
import ru.mercury.vpclient.features.consultant.intent.ConsultantIntent
import ru.mercury.vpclient.features.consultant.model.ConsultantModel
import ru.mercury.vpclient.features.main.tabs.consultants.api.ConsultantsMockApi
import ru.mercury.vpclient.features.main.tabs.consultants.model.ConsultantUiModel
import ru.mercury.vpclient.main.event.MainEventManager
import javax.inject.Inject

@HiltViewModel
class ConsultantViewModel @Inject constructor(): ClientViewModel<ConsultantIntent, ConsultantModel, Event>(ConsultantModel()) {

    override fun dispatch(intent: ConsultantIntent) {
        when (intent) {
            is ConsultantIntent.LoadConsultant -> reduce {
                it.copy(
                    consultant = ConsultantsMockApi.getConsultants().firstOrNull { consultant ->
                        consultant.id == intent.consultantId
                    } ?: ConsultantUiModel.Empty
                )
            }
            is ConsultantIntent.BackClick -> launch { MainEventManager.send(BackRoute) }
        }
    }
}
