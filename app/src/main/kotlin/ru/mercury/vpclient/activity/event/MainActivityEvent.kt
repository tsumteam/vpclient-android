package ru.mercury.vpclient.activity.event

import ru.mercury.vpclient.shared.mvi.Event

sealed interface MainActivityEvent: Event {
    data object RequestPushNotificationsPermission: MainActivityEvent
}
