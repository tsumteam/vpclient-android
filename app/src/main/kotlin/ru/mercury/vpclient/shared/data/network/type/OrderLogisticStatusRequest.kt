package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class OrderLogisticStatusRequest {
    @SerialName("created") CREATED,
    @SerialName("approved") APPROVED,
    @SerialName("kitting") KITTING,
    @SerialName("readyToShipToCustomer") READY_TO_SHIP_TO_CUSTOMER,
    @SerialName("shippingToCustomer") SHIPPING_TO_CUSTOMER,
    @SerialName("atCustomer") AT_CUSTOMER,
    @SerialName("sold") SOLD,
    @SerialName("notSold") NOT_SOLD,
    @SerialName("notAvailable") NOT_AVAILABLE,
    @SerialName("inTheStore") IN_THE_STORE
}
