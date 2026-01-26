package ru.mercury.vpclient.core.persistence.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import ru.mercury.vpclient.core.DeliveryId
import ru.mercury.vpclient.core.PaymentMethodId
import ru.mercury.vpclient.core.RouteId

@Entity(
    tableName = "paymentMethods",
    primaryKeys = ["paymentMethodId", "deliveryId"],
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
        Index("deliveryId")
    ]
)
data class PaymentMethodEntity(
    val routeId: RouteId,
    val deliveryId: DeliveryId,
    val paymentMethodId: PaymentMethodId,
    val paymentName: String,
    val paymentType: String
) {
    companion object {
        val Empty = PaymentMethodEntity(
            routeId = "",
            deliveryId = "",
            paymentMethodId = "",
            paymentName = "",
            paymentType = ""
        )
    }
}
