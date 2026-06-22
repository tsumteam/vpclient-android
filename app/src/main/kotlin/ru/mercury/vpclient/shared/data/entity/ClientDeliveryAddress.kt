package ru.mercury.vpclient.shared.data.entity

data class ClientDeliveryAddress(
    val id: Int,
    val address: String,
    val flat: String = "",
    val entrance: String = "",
    val intercom: String = "",
    val floor: String = "",
    val comment: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null
) {
    val title: String
        get() = listOf(
            address,
            flat.takeIf { it.isNotBlank() }?.let { "кв./офис $it" }
        ).filterNotNull().joinToString(", ")
}
