package ru.mercury.vpclient.features.home.event

import ru.mercury.vpclient.shared.mvi.Event

sealed interface HomeEvent: Event {
    data class SnackbarErrorMessage(val message: String): HomeEvent
}
