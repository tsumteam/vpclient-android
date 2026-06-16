package ru.mercury.vpclient.features.profile_loyalty_info.intent

import ru.mercury.vpclient.shared.data.entity.LoyaltyCardType
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface ProfileLoyaltyInfoIntent: Intent {
    data object LoadData: ProfileLoyaltyInfoIntent
    data object BackClick: ProfileLoyaltyInfoIntent
    data object QrClick: ProfileLoyaltyInfoIntent
    data object MoreClick: ProfileLoyaltyInfoIntent
    data object UnlinkClick: ProfileLoyaltyInfoIntent
    data object DismissUnlinkDialog: ProfileLoyaltyInfoIntent
    data object ConfirmUnlinkClick: ProfileLoyaltyInfoIntent
    data class CardTypeClick(val type: LoyaltyCardType): ProfileLoyaltyInfoIntent
}
