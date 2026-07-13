package ru.mercury.vpclient.features.brand_root.event

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

object BrandRootEventManager {

    private val _eventChannel = Channel<Any>()
    val eventFlow: Flow<Any> = _eventChannel.receiveAsFlow()

    suspend fun send(element: Any) {
        return _eventChannel.send(element)
    }
}
