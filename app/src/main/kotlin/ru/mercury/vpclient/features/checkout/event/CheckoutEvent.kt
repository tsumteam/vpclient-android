package ru.mercury.vpclient.features.checkout.event

import ru.mercury.vpclient.shared.mvi.Event

sealed interface CheckoutEvent: Event {
    data class SnackbarErrorMessage(val message: String): CheckoutEvent
    data class SnackbarTopErrorMessage(val message: String): CheckoutEvent
    data class OpenPaymentUrl(val url: String): CheckoutEvent
}
