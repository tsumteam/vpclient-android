package ru.mercury.vpclient.features.gift_card.event

import ru.mercury.vpclient.shared.mvi.Event

sealed interface GiftCardEvent: Event {
    data class SnackbarErrorMessage(val message: String): GiftCardEvent
}
