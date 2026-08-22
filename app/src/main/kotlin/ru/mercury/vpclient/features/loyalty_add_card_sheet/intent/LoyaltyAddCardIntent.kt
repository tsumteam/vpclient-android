package ru.mercury.vpclient.features.loyalty_add_card_sheet.intent

import ru.mercury.vpclient.shared.data.entity.LoyaltyAddCardMode
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface LoyaltyAddCardIntent: Intent {
    data object DismissClick: LoyaltyAddCardIntent
    data object ConfirmClick: LoyaltyAddCardIntent
    data class ModeClick(val mode: LoyaltyAddCardMode): LoyaltyAddCardIntent
    data class PhoneChange(val phone: String): LoyaltyAddCardIntent
    data class CardNumberChange(val cardNumber: String): LoyaltyAddCardIntent
}
