package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class EmployeeNotificationType {
    @SerialName("fitting") FITTING,
    @SerialName("order") ORDER,
    @SerialName("stockAvailability") STOCK_AVAILABILITY,
    @SerialName("sync") SYNC,
    @SerialName("newArrivals") NEW_ARRIVALS,
    @SerialName("newAction") NEW_ACTION,
    @SerialName("newCatalog") NEW_CATALOG
}
