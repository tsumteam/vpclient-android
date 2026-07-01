package ru.mercury.vpclient.shared.data.persistence.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ClientDeliveryAddressEntity")
data class ClientDeliveryAddressEntity(
    @PrimaryKey val id: Int,
    val address: String,
    val flat: String = "",
    val entrance: String = "",
    val intercom: String = "",
    val floor: String = "",
    val comment: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null
)
