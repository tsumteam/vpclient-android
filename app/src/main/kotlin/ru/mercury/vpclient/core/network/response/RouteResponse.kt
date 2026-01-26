package ru.mercury.vpclient.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RouteResponse(
    @SerialName("routeId") val routeId: String?,
    @SerialName("routeStatus") val routeStatus: String?,
    @SerialName("driverId") val driverId: String?,
    @SerialName("driverName") val driverName: String?,
    @SerialName("driverBarcode") val driverBarcode: String?,
    @SerialName("cashOnHand") val cashOnHand: Double?,
    @SerialName("deliveries") val deliveries: List<DeliveryResponse>?
) {
    companion object {
        const val ROUTE_STATUS_ASSIGNED = "Assigned"
        const val ROUTE_STATUS_RUNNING = "Running"
        const val ROUTE_STATUS_FINISHED = "Finished"
    }
}
