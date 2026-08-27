package ru.mercury.vpclient.features.messenger_sheet.event

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

object MessengerEventManager {

    private val _eventChannel = Channel<MessengerEvent>()
    val eventFlow: Flow<MessengerEvent> = _eventChannel.receiveAsFlow()

    suspend fun send(event: MessengerEvent) {
        _eventChannel.send(event)
    }
}
