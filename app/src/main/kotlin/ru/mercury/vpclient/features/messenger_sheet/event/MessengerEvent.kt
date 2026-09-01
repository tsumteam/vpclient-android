package ru.mercury.vpclient.features.messenger_sheet.event

import ru.mercury.vpclient.shared.mvi.Event

sealed interface MessengerEvent: Event {
    data object RefreshMessages: MessengerEvent
    data class LaunchDialer(val phone: String): MessengerEvent
    data class SnackbarErrorMessage(val message: String): MessengerEvent
}
