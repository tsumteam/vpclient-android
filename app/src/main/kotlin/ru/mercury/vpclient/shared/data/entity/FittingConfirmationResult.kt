package ru.mercury.vpclient.shared.data.entity

data class FittingConfirmationResult(
    val deliveryLines: List<FittingConfirmationResultDeliveryLine>,
    val address: String
)
