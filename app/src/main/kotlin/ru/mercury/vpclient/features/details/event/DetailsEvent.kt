package ru.mercury.vpclient.features.details.event

import ru.mercury.vpclient.shared.mvi.Event

sealed interface DetailsEvent: Event {
    data class SnackbarErrorMessage(val message: String): DetailsEvent
}
