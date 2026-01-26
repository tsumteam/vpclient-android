package ru.mercury.vpclient.core.persistence.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import ru.mercury.vpclient.core.BoutiqueId
import ru.mercury.vpclient.core.DeliveryId
import ru.mercury.vpclient.core.OrderId
import ru.mercury.vpclient.core.ProductLineId
import ru.mercury.vpclient.core.RouteId

@Entity(
    tableName = "deliveries",
    primaryKeys = ["deliveryId"],
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
data class DeliveryEntity(
    val routeId: RouteId,
    val routeStatus: String,
    val deliveryId: DeliveryId,
    val deliveryStatus: String,
    val detailedStatus: String,
    val codeGoal: String,
    val codeTask: String,
    val isPaid: Boolean,
    val numberOfItems: Int,
    val sourceDocId: String,
    val sourceDocType: String,
    val startTime: String,
    val arrivalFrom: String,
    val arrivalTo: String,
    val sellerName: String,
    val sellerPhone: String,
    val clientName: String,
    val clientPhone: String,
    val clientAddress: String,
    val comment: String,
    val clientCanCall: Boolean,
    val boutiqueId: BoutiqueId,
    val boutiqueName: String,
    val isFitting: Boolean,
    val clientClass: String,
    val extOrderId: OrderId,
    val orderId: OrderId,

    val cargoPackagesLineIds: List<ProductLineId>,
    val position: Int,
    val fittingTimer: Long,
    val isParent: Boolean,
    val isOffline: Boolean,
    val parentDeliveryId: DeliveryId,
    val detailedStatusTrackingStatus: String = "",
    val detailedStatusTrackingTimestampMs: Long = 0L
) {
    companion object {
        const val FITTING_CANCELLED = -1L

        val Empty = DeliveryEntity(
            routeId = "",
            routeStatus = "",
            deliveryId = "",
            deliveryStatus = "",
            detailedStatus = "",
            codeGoal = "",
            codeTask = "",
            isPaid = false,
            numberOfItems = 0,
            sourceDocId = "",
            sourceDocType = "",
            startTime = "",
            arrivalFrom = "",
            arrivalTo = "",
            sellerName = "",
            sellerPhone = "",
            clientName = "",
            clientPhone = "",
            clientAddress = "",
            comment = "",
            clientCanCall = false,
            boutiqueId = "",
            boutiqueName = "",
            isFitting = false,
            clientClass = "",
            extOrderId = "",
            orderId = "",
            cargoPackagesLineIds = emptyList(),
            position = 0,
            fittingTimer = 0L,
            isParent = false,
            isOffline = false,
            parentDeliveryId = "",
            detailedStatusTrackingStatus = "",
            detailedStatusTrackingTimestampMs = 0L
        )
    }
}
