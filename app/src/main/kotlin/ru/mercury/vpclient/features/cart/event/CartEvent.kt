package ru.mercury.vpclient.features.cart.event

import ru.mercury.vpclient.shared.mvi.Event

sealed interface CartEvent: Event {
    data class SnackbarErrorMessage(val message: String): CartEvent
}
