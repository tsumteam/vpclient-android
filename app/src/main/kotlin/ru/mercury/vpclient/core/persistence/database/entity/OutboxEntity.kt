package ru.mercury.vpclient.core.persistence.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import ru.mercury.vpclient.core.RouteId

@Entity(
    tableName = "outbox",
    primaryKeys = ["position"],
    foreignKeys = [
        ForeignKey(
            entity = RouteEntity::class,
            parentColumns = ["routeId"],
            childColumns = ["routeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("routeId")]
)
data class OutboxEntity(
    val routeId: RouteId,
    val endpoint: String,
    val requestJson: String,
    val position: Int,
    val failure: Boolean
) {
    companion object {
        const val ENDPOINT_SET_DELIVERY_STATUS = "set-delivery-status"
        const val ENDPOINT_UPDATE_ORDER_LINE_STATUSES = "update-order-line-statuses"
        const val ENDPOINT_HAND_OVER_CART = "hand‑over-cart"

        val Empty = OutboxEntity(
            routeId = "",
            endpoint = "",
            requestJson = "",
            position = 0,
            failure = false
        )
    }
}
