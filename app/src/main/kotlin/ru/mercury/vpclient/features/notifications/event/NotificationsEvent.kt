package ru.mercury.vpclient.features.notifications.event

import ru.mercury.vpclient.shared.mvi.Event

sealed interface NotificationsEvent: Event {
    data class OpenDeepLink(val deepLinkUrl: String): NotificationsEvent
    data class SnackbarErrorMessage(val message: String): NotificationsEvent
}
