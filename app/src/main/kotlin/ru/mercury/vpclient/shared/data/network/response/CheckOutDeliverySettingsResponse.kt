package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.DeliveryType

@Serializable
data class CheckOutDeliverySettingsResponse(
    @SerialName("deliveryType") val deliveryType: DeliveryType? = null,
    @SerialName("leaveItemsToClient") val leaveItemsToClient: Boolean? = null,
    @SerialName("isGoWithCourier") val isGoWithCourier: Boolean? = null,
    @SerialName("packOrderMyself") val packOrderMyself: Boolean? = null,
    @SerialName("manageClientCommunicationsMyself") val manageClientCommunicationsMyself: Boolean? = null,
    @SerialName("vipShopperManualPickup") val vipShopperManualPickup: Boolean? = null,
    @SerialName("deliveryComment") val deliveryComment: String? = null
)
