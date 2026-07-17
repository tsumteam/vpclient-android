package ru.mercury.vpclient.features.gift_card.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface GiftCardIntent: Intent {
    data object CollectGiftCard: GiftCardIntent
    data object LoadGiftCard: GiftCardIntent
    data object CollectActiveEmployee: GiftCardIntent
    data object BackClick: GiftCardIntent
    data object FittingClick: GiftCardIntent
    data object CartClick: GiftCardIntent
    data object TermsClick: GiftCardIntent
    data object TermsDismiss: GiftCardIntent
    data object BuyClick: GiftCardIntent
    data class SelectTemplate(val index: Int): GiftCardIntent
    data class AmountChange(val value: String): GiftCardIntent
    data class SelectAmount(val amount: Int): GiftCardIntent
}
