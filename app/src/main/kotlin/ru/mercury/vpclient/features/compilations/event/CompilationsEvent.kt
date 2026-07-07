package ru.mercury.vpclient.features.compilations.event

import ru.mercury.vpclient.shared.mvi.Event

sealed interface CompilationsEvent: Event {
    data class SnackbarErrorMessage(val message: String): CompilationsEvent
}
