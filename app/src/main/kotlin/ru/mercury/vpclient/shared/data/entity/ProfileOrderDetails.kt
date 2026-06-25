package ru.mercury.vpclient.shared.data.entity

data class ProfileOrderDetails(
    val orderNumber: String,
    val amount: String,
    val creationDate: String,
    val isPaymentAlertVisible: Boolean,
    val paymentAlertRemainingMinutes: Int,
    val deliveries: List<ProfileOrderDelivery>
)
