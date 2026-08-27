package ru.mercury.vpclient.features.messenger_sheet.event

import ru.mercury.vpclient.shared.mvi.Event

sealed interface MessengerHostEvent: Event {
    data object DismissRequest: MessengerHostEvent
}
