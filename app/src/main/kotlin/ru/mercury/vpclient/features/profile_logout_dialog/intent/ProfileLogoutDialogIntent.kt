package ru.mercury.vpclient.features.profile_logout_dialog.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface ProfileLogoutDialogIntent: Intent {
    data object ConfirmRequest: ProfileLogoutDialogIntent
    data object DismissRequest: ProfileLogoutDialogIntent
}
