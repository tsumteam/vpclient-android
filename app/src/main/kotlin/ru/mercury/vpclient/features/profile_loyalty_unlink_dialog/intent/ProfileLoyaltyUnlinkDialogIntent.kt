package ru.mercury.vpclient.features.profile_loyalty_unlink_dialog.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface ProfileLoyaltyUnlinkDialogIntent: Intent {
    data object ConfirmRequest: ProfileLoyaltyUnlinkDialogIntent
    data object DismissRequest: ProfileLoyaltyUnlinkDialogIntent
}
