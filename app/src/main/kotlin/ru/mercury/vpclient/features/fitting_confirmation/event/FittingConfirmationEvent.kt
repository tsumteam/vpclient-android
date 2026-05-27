package ru.mercury.vpclient.features.fitting_confirmation.event

import ru.mercury.vpclient.shared.mvi.Event

sealed interface FittingConfirmationEvent: Event {
    data class SnackbarMessage(val message: String): FittingConfirmationEvent
}
