package ru.mercury.vpclient.shared.data.entity

import ru.mercury.vpclient.shared.data.network.type.GiftCardType

data class GiftCardPaymentData(
    val itemId: String,
    val templateId: Int,
    val amount: Double,
    val type: GiftCardType,
    val email: String,
    val phone: String,
    val dateFrom: String,
    val dateTo: String
)
