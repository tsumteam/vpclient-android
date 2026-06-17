package ru.mercury.vpclient.features.profile_privileges_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface ProfilePrivilegeIntent: Intent {
    data object DismissRequest: ProfilePrivilegeIntent
}
