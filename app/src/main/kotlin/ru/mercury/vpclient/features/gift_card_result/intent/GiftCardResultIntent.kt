package ru.mercury.vpclient.features.gift_card_result.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface GiftCardResultIntent: Intent {
    data object HomeClick: GiftCardResultIntent
    data object PurchasesClick: GiftCardResultIntent
}
