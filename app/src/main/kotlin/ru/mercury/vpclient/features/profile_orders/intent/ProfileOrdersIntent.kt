package ru.mercury.vpclient.features.profile_orders.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface ProfileOrdersIntent: Intent {
    data object CollectCartCount: ProfileOrdersIntent
    data object CollectFittingCount: ProfileOrdersIntent
    data object CollectActiveEmployee: ProfileOrdersIntent
    data object LoadCartData: ProfileOrdersIntent
    data object PullToRefresh: ProfileOrdersIntent
    data object RefreshCompleted: ProfileOrdersIntent
    data object BackClick: ProfileOrdersIntent
    data object NotificationClick: ProfileOrdersIntent
    data object CartClick: ProfileOrdersIntent
    data object FittingClick: ProfileOrdersIntent
    data object MessengerClick: ProfileOrdersIntent
    data class OrderClick(val orderNumber: String, val amount: String): ProfileOrdersIntent
}
