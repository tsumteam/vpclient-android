package ru.mercury.vpclient.features.fitting_products_sheet.event

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

object FittingProductsEventManager {

    private val _eventChannel = Channel<FittingProductsEvent>()
    val eventFlow: Flow<FittingProductsEvent> = _eventChannel.receiveAsFlow()

    suspend fun send(event: FittingProductsEvent) {
        _eventChannel.send(event)
    }
}
