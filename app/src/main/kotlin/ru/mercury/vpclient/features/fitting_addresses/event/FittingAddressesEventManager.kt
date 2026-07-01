package ru.mercury.vpclient.features.fitting_addresses.event

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

object FittingAddressesEventManager {

    private val _eventChannel = Channel<FittingAddressesEvent>()
    val eventFlow: Flow<FittingAddressesEvent> = _eventChannel.receiveAsFlow()

    suspend fun send(event: FittingAddressesEvent) {
        _eventChannel.send(event)
    }
}
