package ru.mercury.vpclient.features.notifications.intent

import ru.mercury.vpclient.shared.data.entity.ClientNotificationCategory
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface NotificationsIntent: Intent {
    data object CollectNotifications: NotificationsIntent
    data object CollectActiveEmployee: NotificationsIntent
    data object LoadNotifications: NotificationsIntent
    data object ResetNotificationCounter: NotificationsIntent
    data object PullToRefresh: NotificationsIntent
    data object BackClick: NotificationsIntent
    data object CartClick: NotificationsIntent
    data object FittingClick: NotificationsIntent
    data object MessengerClick: NotificationsIntent
    data class SelectCategory(val category: ClientNotificationCategory): NotificationsIntent
    data class NotificationClick(val deepLinkUrl: String): NotificationsIntent
}
