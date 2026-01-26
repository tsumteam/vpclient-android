package ru.mercury.vpclient.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderLineStatusResponse(
    @SerialName("boxes") val boxes: List<BoxResponse>?,
    @SerialName("boxQty") val boxQty: Int?,
    @SerialName("isFitting") val isFitting: Boolean?,
    @SerialName("isPaid") val isPaid: Boolean?,
    @SerialName("orderAmount") val orderAmount: Double?,
    @SerialName("orderAmountWithDisc") val orderAmountWithDisc: Double?,
    @SerialName("orderDisc") val orderDisc: Double?,
    @SerialName("orderLines") val orderLines: List<OrderLineResponse>?
) {
    companion object {
        const val ACTION_TYPE_STAY = "stay" // На примерку
        const val ACTION_TYPE_RETURN = "return"
    }
}
