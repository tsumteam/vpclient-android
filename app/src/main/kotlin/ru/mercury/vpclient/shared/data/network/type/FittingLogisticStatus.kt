package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class FittingLogisticStatus {
    @SerialName("created") CREATED,
    @SerialName("approved") APPROVED,
    @SerialName("kitting") KITTING,
    @SerialName("readyToShipToCustomer") READY_TO_SHIP_TO_CUSTOMER,
    @SerialName("shippingToCustomer") SHIPPING_TO_CUSTOMER,
    @SerialName("atCustomer") AT_CUSTOMER,
    @SerialName("returningFromCustomer") RETURNING_FROM_CUSTOMER,
    @SerialName("inTheStore") IN_THE_STORE,
    @SerialName("notAvailable") NOT_AVAILABLE,
    @SerialName("refused") REFUSED
}
