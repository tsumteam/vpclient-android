package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.FittingOperationInitiator

@Serializable
data class FittingResponse(
    @SerialName("fittingNumber") val fittingNumber: String? = null,
    @SerialName("id") val id: String? = null,
    @SerialName("axaptaId") val axaptaId: String? = null,
    @SerialName("initiator") val initiator: FittingOperationInitiator? = null,
    @SerialName("deliveries") val deliveries: List<FittingDeliveryResponse>? = null,
    @SerialName("returningProducts") val returningProducts: List<FittingReturningProductResponse>? = null,
    @SerialName("catalogActionDisclaimer") val catalogActionDisclaimer: String? = null,
    @SerialName("timestamp") val timestamp: String? = null,
    @SerialName("version") val version: Int? = null
)
