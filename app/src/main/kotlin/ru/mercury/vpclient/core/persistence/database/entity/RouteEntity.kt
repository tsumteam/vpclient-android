package ru.mercury.vpclient.core.persistence.database.entity

import androidx.room.Entity
import ru.mercury.vpclient.core.Amount
import ru.mercury.vpclient.core.RouteId

@Entity(tableName = "routes", primaryKeys = ["routeId"])
data class RouteEntity(
    val routeId: RouteId,
    val routeStatus: String,
    val driverId: String,
    val driverName: String,
    val driverBarcode: String,
    val cashOnHand: Amount
) {
    companion object {
        val Empty = RouteEntity(
            routeId = "",
            routeStatus = "",
            driverId = "",
            driverName = "",
            driverBarcode = "",
            cashOnHand = 0.0
        )
    }
}
