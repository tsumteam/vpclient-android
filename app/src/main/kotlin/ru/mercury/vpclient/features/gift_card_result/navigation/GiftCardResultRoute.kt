package ru.mercury.vpclient.features.gift_card_result.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class GiftCardResultRoute(
    val isPaid: Boolean,
    val orderNumber: String,
    val email: String,
    val phone: String,
    val deliveryDate: String,
    val deliveryTime: String
): NavKey
