package ru.mercury.vpclient.features.code.event

import ru.mercury.vpclient.shared.mvi.Event

sealed interface CodeEvents: Event {
    data object ClearFocus: CodeEvents
    data class SnackbarMessage(val message: String): CodeEvents
}
