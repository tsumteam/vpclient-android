package ru.mercury.vpclient.features.main.tabs.consultants

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mercury.vpclient.core.mvi.ClientViewModel
import ru.mercury.vpclient.core.mvi.Event
import ru.mercury.vpclient.features.main.tabs.consultants.api.ConsultantsMockApi
import ru.mercury.vpclient.features.main.tabs.consultants.intent.ConsultantsIntent
import ru.mercury.vpclient.features.main.tabs.consultants.model.ConsultantsModel
import javax.inject.Inject

@HiltViewModel
class ConsultantsViewModel @Inject constructor(

): ClientViewModel<ConsultantsIntent, ConsultantsModel, Event>(ConsultantsModel()) {

    init {
        dispatch(ConsultantsIntent.LoadConsultants)
    }

    override fun dispatch(intent: ConsultantsIntent) {
        when (intent) {
            is ConsultantsIntent.LoadConsultants -> launch { reduce { it.copy(consultants = ConsultantsMockApi.getConsultants()) } }
            is ConsultantsIntent.SetActiveConsultant -> reduce {
                it.copy(
                    consultants = it.consultants.map { consultant ->
                        consultant.copy(isActive = consultant.id == intent.consultantId)
                    }
                )
            }
        }
    }
}
