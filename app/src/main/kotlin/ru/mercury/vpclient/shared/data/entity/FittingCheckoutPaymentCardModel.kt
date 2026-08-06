package ru.mercury.vpclient.shared.data.entity

data class FittingCheckoutPaymentCardModel(
    val id: String,
    val paymentSystem: String,
    val lastFourDigits: String,
    val isSelected: Boolean = false
)
