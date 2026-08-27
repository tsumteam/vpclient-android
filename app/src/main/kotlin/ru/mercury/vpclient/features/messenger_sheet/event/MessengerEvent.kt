package ru.mercury.vpclient.features.messenger_sheet.event

import ru.mercury.vpclient.shared.mvi.Event

sealed interface MessengerEvent: Event {
    data object DismissRequest: MessengerEvent
}
