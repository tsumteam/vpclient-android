package ru.mercury.vpclient.features.profile_order.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface ProfileOrderIntent: Intent {
    data object CollectRoute: ProfileOrderIntent
    data object LoadData: ProfileOrderIntent
    data object BackClick: ProfileOrderIntent
    data class DeliveryClick(val deliveryId: String, val productIds: List<String>): ProfileOrderIntent
    data class ProductClick(val productId: String): ProfileOrderIntent
}
