package ru.mercury.vpclient.features.consultant.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface ConsultantIntent: Intent {
    data object CollectConsultant: ConsultantIntent
    data object LoadConsultant: ConsultantIntent
    data object BackClick: ConsultantIntent
}
