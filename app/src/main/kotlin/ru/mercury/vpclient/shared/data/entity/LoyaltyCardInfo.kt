package ru.mercury.vpclient.shared.data.entity

data class LoyaltyCardInfo(
    val loyaltyCardNumber: String = "",
    val bonusAmount: Int = 0,
    val clientName: String = "",
    val typeCard: LoyaltyCardType = LoyaltyCardType.Black,
    val qrCode: String = ""
)
