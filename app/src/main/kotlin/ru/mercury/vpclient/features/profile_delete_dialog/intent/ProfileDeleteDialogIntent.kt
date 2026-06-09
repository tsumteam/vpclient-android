package ru.mercury.vpclient.features.profile_delete_dialog.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface ProfileDeleteDialogIntent: Intent {
    data object ConfirmRequest: ProfileDeleteDialogIntent
    data object DismissRequest: ProfileDeleteDialogIntent
}
