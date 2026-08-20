package ru.mercury.vpclient.features.profile_loyalty_unlink_dialog.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface ProfileLoyaltyUnlinkIntent: Intent {
    data object DismissRequest: ProfileLoyaltyUnlinkIntent
    data object ConfirmRequest: ProfileLoyaltyUnlinkIntent
}
