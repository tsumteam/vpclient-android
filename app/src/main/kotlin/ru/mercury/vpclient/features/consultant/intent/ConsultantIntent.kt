package ru.mercury.vpclient.features.consultant.intent

import ru.mercury.vpclient.core.mvi.Intent

sealed interface ConsultantIntent: Intent {
    data object BackClick: ConsultantIntent
    data class LoadConsultant(val consultantId: String): ConsultantIntent
}
