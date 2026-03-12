package ru.mercury.vpclient.features.debug.event

import ru.mercury.vpclient.core.mvi.Event

sealed interface DebugEvent: Event {
    data object FinishScreen: DebugEvent
    data class SnackbarMessage(val message: String): DebugEvent
}
