package ru.mercury.vpclient.core.persistence.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import ru.mercury.vpclient.core.Amount
import ru.mercury.vpclient.core.DeliveryId
import ru.mercury.vpclient.core.PaymentId
import ru.mercury.vpclient.core.PaymentMethodId
import ru.mercury.vpclient.core.RouteId
import ru.mercury.vpclient.core.TrxId

@Entity(
    tableName = "payments",
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
data class PaymentEntity(
    val routeId: RouteId,
    val deliveryId: DeliveryId,
    val paymentMethodId: PaymentMethodId,
    val paymentName: String,
    val paymentType: String,
    val amount: Amount,
    val paymentId: PaymentId,
    val qrCodeBase64: String,
    val status: String,
    val trxId: TrxId,
    val loyaltyCardNumber: String
) {
    companion object {
        val Empty = PaymentEntity(
            routeId = "",
            deliveryId = "",
            paymentMethodId = "",
            paymentName = "",
            paymentType = "",
            amount = 0.0,
            paymentId = "",
            qrCodeBase64 = "",
            status = "",
            trxId = "",
            loyaltyCardNumber = ""
        )
    }
}
