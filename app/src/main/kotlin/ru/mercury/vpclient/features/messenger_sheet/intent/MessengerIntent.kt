package ru.mercury.vpclient.features.messenger_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface MessengerIntent: Intent {
    data object DismissClick: MessengerIntent
    data class SendClick(val text: String): MessengerIntent
}
