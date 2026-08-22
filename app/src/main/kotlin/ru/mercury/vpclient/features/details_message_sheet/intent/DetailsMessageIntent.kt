package ru.mercury.vpclient.features.details_message_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface DetailsMessageIntent: Intent {
    data object DismissClick: DetailsMessageIntent
    data class SendClick(val comment: String): DetailsMessageIntent
}
