package ru.mercury.vpclient.shared.data.entity

data class FittingConfirmationDeliveryInterval(
    val id: String,
    val dayId: String,
    val dayTitle: String,
    val timeTitle: String,
    val summary: String,
    val from: String? = null,
    val to: String? = null
)
