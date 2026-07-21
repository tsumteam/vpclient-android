package ru.mercury.vpclient.activity.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface MainActivityIntent: Intent {
    data object ResolveNavigation: MainActivityIntent
    data object PushNotificationsSheetEnableClick: MainActivityIntent
    data object PushNotificationsSheetDismissClick: MainActivityIntent
    data class CenterLoading(val enabled: Boolean): MainActivityIntent
}
