package ru.mercury.vpclient.features.profile_loyalty_info.intent

import ru.mercury.vpclient.features.profile_loyalty_unlink_dialog.intent.ProfileLoyaltyUnlinkIntent
import ru.mercury.vpclient.shared.data.entity.LoyaltyCardType
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface ProfileLoyaltyInfoIntent: Intent {
    data object LoadData: ProfileLoyaltyInfoIntent
    data object BackClick: ProfileLoyaltyInfoIntent
    data object QrClick: ProfileLoyaltyInfoIntent
    data object MoreClick: ProfileLoyaltyInfoIntent
    data object UnlinkClick: ProfileLoyaltyInfoIntent
    data class CardTypeClick(val type: LoyaltyCardType): ProfileLoyaltyInfoIntent
    data class OnProfileLoyaltyUnlinkIntent(val intent: ProfileLoyaltyUnlinkIntent): ProfileLoyaltyInfoIntent
}
