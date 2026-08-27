package ru.mercury.vpclient.features.messenger_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface MessengerIntent: Intent {
    data object DismissClick: MessengerIntent
    data object CollectActiveEmployee: MessengerIntent
    data object CallClick: MessengerIntent
    data object AttachClick: MessengerIntent
    data object MicClick: MessengerIntent
    data object SendClick: MessengerIntent
    data class MessageTextChange(val text: String): MessengerIntent
}
