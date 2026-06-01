package ru.mercury.vpclient.features.fitting_address_selection.event

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

object FittingAddressSelectionResultManager {

    private val _resultChannel = Channel<FittingAddressSelectionResult>()
    val resultFlow: Flow<FittingAddressSelectionResult> = _resultChannel.receiveAsFlow()

    suspend fun send(result: FittingAddressSelectionResult) {
        _resultChannel.send(result)
    }
}
