package ru.mercury.vpclient.features.push_notifications_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface PushNotificationsIntent: Intent {
    data object DismissClick: PushNotificationsIntent
    data object EnableClick: PushNotificationsIntent
}
