package ru.mercury.vpclient.features.gift_card_checkout.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.GiftCardType

@Serializable
data class GiftCardCheckoutRoute(
    val itemId: String,
    val amount: Int,
    val templateId: Int,
    val type: GiftCardType
): NavKey
