package ru.mercury.vpclient.shared.data.entity

data class FittingData(
    val deliveries: List<FittingDeliveryData> = emptyList()
) {
    val products: List<CartProduct>
        get() {
            return deliveries.flatMap { delivery -> delivery.products }
        }
}
