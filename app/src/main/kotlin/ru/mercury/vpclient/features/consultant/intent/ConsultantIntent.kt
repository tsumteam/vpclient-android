package ru.mercury.vpclient.features.consultant.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface ConsultantIntent: Intent {
    data object CollectEmployee: ConsultantIntent
    data object LoadEmployee: ConsultantIntent
    data object BackClick: ConsultantIntent
    data class ActionClick(val actionId: Int): ConsultantIntent
}
