package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.FittingType
import ru.mercury.vpclient.shared.data.network.type.DeliveryType
import ru.mercury.vpclient.shared.data.network.type.KittingType

@Serializable
data class BasketGetDeliveryTimesForOrderRequest(
    @SerialName("pairedUserId") val pairedUserId: String? = null,
    @SerialName("fittingType") val fittingType: FittingType? = null,
    @SerialName("deliveryType") val deliveryType: DeliveryType? = null,
    @SerialName("kittingType") val kittingType: KittingType? = null,
    @SerialName("address") val address: String? = null,
    @SerialName("addressComment") val addressComment: String? = null
)
