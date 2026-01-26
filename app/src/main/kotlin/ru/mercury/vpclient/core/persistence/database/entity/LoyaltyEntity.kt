package ru.mercury.vpclient.core.persistence.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import ru.mercury.vpclient.core.Amount
import ru.mercury.vpclient.core.DeliveryId
import ru.mercury.vpclient.core.RouteId

@Entity(
    tableName = "loyalty",
    primaryKeys = ["deliveryId"],
    foreignKeys = [
        ForeignKey(
            entity = RouteEntity::class,
            parentColumns = ["routeId"],
            childColumns = ["routeId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = DeliveryEntity::class,
            parentColumns = ["deliveryId"],
            childColumns = ["deliveryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("routeId"),
        Index(value = ["deliveryId"], unique = true)
    ]
)
data class LoyaltyEntity(
    val routeId: RouteId,
    val deliveryId: DeliveryId,
    val card: String,
    val token: String,
    val confirmedAmount: Amount,
    val isCorrectPin: Boolean,
    val pinTimer: Long
) {
    companion object {
        val Empty = LoyaltyEntity(
            routeId = "",
            deliveryId = "",
            card = "",
            token = "",
            confirmedAmount = 0.0,
            isCorrectPin = false,
            pinTimer = 0L
        )
    }
}
