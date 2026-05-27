package ru.mercury.vpclient.shared.data.entity

data class FittingConfirmationData(
    val boutiqueAddress: String? = null,
    val clientAddress: String? = null,
    val isClientAddressAvailable: Boolean = true,
    val singleIntervals: List<FittingConfirmationDeliveryInterval> = emptyList(),
    val deliveryGroups: List<FittingConfirmationDeliveryGroup> = emptyList()
)
