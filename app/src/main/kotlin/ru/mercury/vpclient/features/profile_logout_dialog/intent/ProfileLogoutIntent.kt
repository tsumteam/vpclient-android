package ru.mercury.vpclient.features.profile_logout_dialog.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface ProfileLogoutIntent: Intent {
    data object ConfirmRequest: ProfileLogoutIntent
    data object DismissRequest: ProfileLogoutIntent
}
