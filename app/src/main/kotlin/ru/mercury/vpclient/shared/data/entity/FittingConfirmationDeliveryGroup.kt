package ru.mercury.vpclient.shared.data.entity

data class FittingConfirmationDeliveryGroup(
    val id: String,
    val products: List<CartProduct>,
    val intervals: List<FittingConfirmationDeliveryInterval>
)
