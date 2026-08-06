package ru.mercury.vpclient.shared.data.entity

data class FittingCheckoutData(
    val deliveryIds: List<String> = emptyList(),
    val itemCount: Int = 0,
    val orderAmount: Int = 0,
    val promotionDiscount: Int = 0,
    val availableBonusAmount: Int = 0,
    val totalAvailableBonusAmount: Int = 0,
    val loyaltyCardNumber: String? = null
) {
    companion object {
        val Empty = FittingCheckoutData()
    }
}
