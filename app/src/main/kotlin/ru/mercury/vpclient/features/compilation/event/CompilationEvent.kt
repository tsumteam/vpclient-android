package ru.mercury.vpclient.features.compilation.event

import ru.mercury.vpclient.shared.mvi.Event

sealed interface CompilationEvent: Event {
    data class SnackbarErrorMessage(val message: String): CompilationEvent
}
