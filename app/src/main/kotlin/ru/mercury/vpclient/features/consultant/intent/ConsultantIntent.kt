package ru.mercury.vpclient.features.consultant.intent

import ru.mercury.vpclient.core.mvi.Intent

sealed interface ConsultantIntent: Intent {
    data object CollectConsultant: ConsultantIntent
    data object LoadConsultant: ConsultantIntent
    data object BackClick: ConsultantIntent
}
