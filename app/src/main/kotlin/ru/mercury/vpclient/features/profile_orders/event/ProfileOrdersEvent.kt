package ru.mercury.vpclient.features.profile_orders.event

import ru.mercury.vpclient.shared.mvi.Event

sealed interface ProfileOrdersEvent: Event {
    data object RefreshOrders: ProfileOrdersEvent
}
