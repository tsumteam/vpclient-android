package ru.mercury.vpclient.core.persistence.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import ru.mercury.vpclient.core.BoutiqueId
import ru.mercury.vpclient.core.DeliveryId
import ru.mercury.vpclient.core.ProductLineId
import ru.mercury.vpclient.core.RouteId

@Entity(
    tableName = "cargoPackages",
    primaryKeys = ["barcode", "deliveryId"],
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
        Index("boutiqueId"),
        Index("deliveryId")
    ]
)
data class CargoEntity(
    val routeId: RouteId,
    val boutiqueId: BoutiqueId,
    val deliveryId: DeliveryId,
    val barcode: String,
    val lineIds: List<ProductLineId>,

    val scannedInBoutique: Boolean,
    val scannedAtDelivery: Boolean
) {
    companion object {
        val Empty = CargoEntity(
            routeId = "",
            boutiqueId = "",
            deliveryId = "",
            barcode = "",
            lineIds = emptyList(),
            scannedInBoutique = false,
            scannedAtDelivery = false
        )
    }
}
