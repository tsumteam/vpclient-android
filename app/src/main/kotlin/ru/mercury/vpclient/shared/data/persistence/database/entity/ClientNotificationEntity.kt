package ru.mercury.vpclient.shared.data.persistence.database.entity

import androidx.room.Entity
import ru.mercury.vpclient.shared.data.entity.ClientNotificationCategory

@Entity(
    tableName = "ClientNotification",
    primaryKeys = ["id", "category"]
)
data class ClientNotificationEntity(
    val id: Int,
    val category: ClientNotificationCategory,
    val badge: Int,
    val type: Int,
    val imageUrl: String,
    val title: String,
    val message: String,
    val deepLinkUrl: String,
    val timestamp: String
)
