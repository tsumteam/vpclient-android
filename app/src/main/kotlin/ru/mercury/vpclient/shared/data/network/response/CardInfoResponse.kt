package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.Serializable

@Serializable
data class CardInfoResponse(
    val loyaltyCardNumber: String? = null,
    val bonusAmount: Double? = null,
    val clientName: String? = null,
    val maxPercent: Double? = null,
    val actionID: String? = null,
    val typeCard: String? = null,
    val dateEndCard: String? = null,
    val qrCode: String? = null
)
