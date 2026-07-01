package ru.mercury.vpclient.features.fitting_products_sheet.event

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

object FittingProductsSheetEventManager {

    private val _eventChannel = Channel<FittingProductsSheetEvent>()
    val eventFlow: Flow<FittingProductsSheetEvent> = _eventChannel.receiveAsFlow()

    suspend fun send(event: FittingProductsSheetEvent) {
        _eventChannel.send(event)
    }
}
