package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.FittingType
import ru.mercury.vpclient.shared.data.network.type.KittingType
import ru.mercury.vpclient.shared.data.network.type.DeliveryType

@Serializable
data class GetDeliveryIntervalsForExistingFittingRequest(
    @SerialName("pairedUserId") val pairedUserId: String? = null,
    @SerialName("deliveryId") val deliveryId: String? = null,
    @SerialName("fittingLineIds") val fittingLineIds: List<String>? = null,
    @SerialName("fittingType") val fittingType: FittingType? = null,
    @SerialName("kittingType") val kittingType: KittingType? = null,
    @SerialName("deliveryType") val deliveryType: DeliveryType? = null,
    @SerialName("address") val address: String? = null,
    @SerialName("addressComment") val addressComment: String? = null
)
