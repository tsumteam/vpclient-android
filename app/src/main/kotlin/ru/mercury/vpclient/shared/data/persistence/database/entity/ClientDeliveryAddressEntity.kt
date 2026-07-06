package ru.mercury.vpclient.shared.data.persistence.database.entity

import androidx.room.Entity

@Entity(
    tableName = "ClientDeliveryAddressEntity",
    primaryKeys = ["id"]
)
data class ClientDeliveryAddressEntity(
    val id: Int,
    val address: String,
    val flat: String = "",
    val entrance: String = "",
    val intercom: String = "",
    val floor: String = "",
    val comment: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null
)
