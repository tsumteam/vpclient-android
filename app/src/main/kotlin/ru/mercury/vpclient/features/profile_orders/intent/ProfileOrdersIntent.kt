package ru.mercury.vpclient.features.profile_orders.intent

import ru.mercury.vpclient.shared.mvi.Intent
import ru.mercury.vpclient.shared.ui.components.profile.ProfileOrderItemState

sealed interface ProfileOrdersIntent: Intent {
    data object CollectCartCount: ProfileOrdersIntent
    data object CollectFittingCount: ProfileOrdersIntent
    data object CollectActiveEmployee: ProfileOrdersIntent
    data object LoadCartData: ProfileOrdersIntent
    data object PullToRefresh: ProfileOrdersIntent
    data object RefreshCompleted: ProfileOrdersIntent
    data object BackClick: ProfileOrdersIntent
    data object CartClick: ProfileOrdersIntent
    data object FittingClick: ProfileOrdersIntent
    data object MessengerClick: ProfileOrdersIntent
    data class OrderClick(val state: ProfileOrderItemState): ProfileOrdersIntent
}
