package ru.mercury.vpclient.features.push_notifications_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface PushNotificationsSheetIntent: Intent {
    data object EnableClick: PushNotificationsSheetIntent
    data object DismissClick: PushNotificationsSheetIntent
}
