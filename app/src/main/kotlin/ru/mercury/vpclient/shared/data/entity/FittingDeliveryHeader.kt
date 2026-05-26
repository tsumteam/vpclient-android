package ru.mercury.vpclient.shared.data.entity

data class FittingDeliveryHeader(
    val status: String,
    val date: String,
    val address: String,
    val isDelivered: Boolean
) {
    companion object {
        val Empty = FittingDeliveryHeader(
            status = "",
            date = "Время доставки не указано",
            address = "Адрес доставки не указан",
            isDelivered = false
        )
    }
}
