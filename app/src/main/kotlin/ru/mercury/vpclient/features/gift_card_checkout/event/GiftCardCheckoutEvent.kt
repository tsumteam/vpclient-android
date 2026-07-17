package ru.mercury.vpclient.features.gift_card_checkout.event

import ru.mercury.vpclient.shared.mvi.Event

sealed interface GiftCardCheckoutEvent: Event {
    data class SnackbarErrorMessage(val message: String): GiftCardCheckoutEvent
    data class OpenPaymentUrl(val url: String): GiftCardCheckoutEvent
}
