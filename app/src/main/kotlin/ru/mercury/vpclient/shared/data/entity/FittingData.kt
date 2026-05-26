package ru.mercury.vpclient.shared.data.entity

data class FittingData(
    val products: List<CartProduct> = emptyList(),
    val deliveryHeader: FittingDeliveryHeader = FittingDeliveryHeader.Empty
)
