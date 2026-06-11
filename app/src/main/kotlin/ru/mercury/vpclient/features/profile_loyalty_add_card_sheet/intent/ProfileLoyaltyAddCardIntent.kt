package ru.mercury.vpclient.features.profile_loyalty_add_card_sheet.intent

import ru.mercury.vpclient.features.profile_loyalty_add_card_sheet.model.ProfileLoyaltyAddCardMode
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface ProfileLoyaltyAddCardIntent: Intent {
    data object DismissRequest: ProfileLoyaltyAddCardIntent
    data class ModeClick(val mode: ProfileLoyaltyAddCardMode): ProfileLoyaltyAddCardIntent
    data class PhoneChange(val phone: String): ProfileLoyaltyAddCardIntent
    data class CardNumberChange(val cardNumber: String): ProfileLoyaltyAddCardIntent
    data object ConfirmClick: ProfileLoyaltyAddCardIntent
}
