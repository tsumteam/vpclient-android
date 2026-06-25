package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.FittingType

@Serializable
data class GetCheckOutFlagsForExistingFittingRequest(
    @SerialName("pairedUserId") val pairedUserId: String? = null,
    @SerialName("deliveryId") val deliveryId: String? = null,
    @SerialName("address") val address: String? = null,
    @SerialName("comment") val comment: String? = null,
    @SerialName("fittingType") val fittingType: FittingType? = null
)
