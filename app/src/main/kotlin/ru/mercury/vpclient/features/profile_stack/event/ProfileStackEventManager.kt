package ru.mercury.vpclient.features.profile_stack.event

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

object ProfileStackEventManager {

    private val _eventChannel = Channel<Any>()
    val eventFlow: Flow<Any> = _eventChannel.receiveAsFlow()

    suspend fun send(element: Any) {
        _eventChannel.send(element)
    }
}
