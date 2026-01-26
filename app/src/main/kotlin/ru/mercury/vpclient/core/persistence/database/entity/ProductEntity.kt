package ru.mercury.vpclient.core.persistence.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import ru.mercury.vpclient.core.Amount
import ru.mercury.vpclient.core.BoutiqueId
import ru.mercury.vpclient.core.DeliveryId
import ru.mercury.vpclient.core.ProductLineId
import ru.mercury.vpclient.core.RouteId

@Entity(
    tableName = "products",
    primaryKeys = ["lineId", "deliveryId"],
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
data class ProductEntity(
    val routeId: RouteId,
    val boutiqueId: BoutiqueId,
    val deliveryId: DeliveryId,
    val cis: String,
    val barcode: String,
    val itemColorId: String,
    val itemId: String,
    val itemName: String,
    val itemSizeId: String,
    val lineId: ProductLineId,
    val price: Amount,
    val priceWithDisc: Amount,
    val quantity: Int,
    val status: String,
    val imageUrl: String,

    val nameAlias: String,
    val submarkName: String,

    val position: Int,
    val isToPay: Boolean
) {
    companion object {
        val Empty = ProductEntity(
            routeId = "",
            boutiqueId = "",
            deliveryId = "",
            cis = "",
            barcode = "",
            itemColorId = "",
            itemId = "",
            itemName = "",
            itemSizeId = "",
            lineId = "",
            price = 0.0,
            priceWithDisc = 0.0,
            quantity = 0,
            status = "",
            imageUrl = "",
            nameAlias = "",
            submarkName = "",
            position = 0,
            isToPay = false
        )
    }
}
