package ru.mercury.vpclient.features.main.tabs.consultants.intent

import ru.mercury.vpclient.core.mvi.Intent

sealed interface ConsultantsIntent: Intent {
    data object LoadConsultants: ConsultantsIntent
    data class SetActiveConsultant(val consultantId: String): ConsultantsIntent
}
