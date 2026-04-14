package ru.mercury.vpclient.features.main.tabs.consultants.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface ConsultantsIntent: Intent {
    data object CollectEmployees: ConsultantsIntent
    data object LoadConsultants: ConsultantsIntent
    data class SetActiveConsultant(val consultantId: String): ConsultantsIntent
    data class ConsultantClick(val consultantId: String): ConsultantsIntent
}
