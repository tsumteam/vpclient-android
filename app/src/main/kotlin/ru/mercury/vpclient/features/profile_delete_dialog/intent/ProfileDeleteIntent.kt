package ru.mercury.vpclient.features.profile_delete_dialog.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface ProfileDeleteIntent: Intent {
    data object ConfirmRequest: ProfileDeleteIntent
    data object DismissRequest: ProfileDeleteIntent
}
