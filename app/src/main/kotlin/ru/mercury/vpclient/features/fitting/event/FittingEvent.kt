package ru.mercury.vpclient.features.fitting.event

import ru.mercury.vpclient.shared.mvi.Event

sealed interface FittingEvent: Event {
    data class SnackbarErrorMessage(val message: String): FittingEvent
}
