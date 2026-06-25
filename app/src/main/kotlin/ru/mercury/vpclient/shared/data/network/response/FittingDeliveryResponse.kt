package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.DeliveryType
import ru.mercury.vpclient.shared.data.network.type.FittingType
import ru.mercury.vpclient.shared.data.network.type.KittingType

@Serializable
data class FittingDeliveryResponse(
    @SerialName("deliveryId") val deliveryId: String? = null,
    @SerialName("lines") val lines: List<FittingLineResponse>? = null,
    @SerialName("order") val order: Int? = null,
    @SerialName("address") val address: String? = null,
    @SerialName("addressComment") val addressComment: String? = null,
    @SerialName("fittingType") val fittingType: FittingType? = null,
    @SerialName("controls") val controls: FittingDeliveryControlsResponse? = null,
    @SerialName("deliveryTime") val deliveryTime: DeliveryTimeResponse? = null,
    @SerialName("deliveryType") val deliveryType: DeliveryType? = null,
    @SerialName("kittingType") val kittingType: KittingType? = null,
    @SerialName("leaveItemsToClient") val leaveItemsToClient: Boolean? = null,
    @SerialName("isGoWithCourier") val isGoWithCourier: Boolean? = null,
    @SerialName("packOrderMyself") val packOrderMyself: Boolean? = null,
    @SerialName("manageClientCommunicationsMyself") val manageClientCommunicationsMyself: Boolean? = null,
    @SerialName("deliveryComment") val deliveryComment: String? = null,
    @SerialName("deliveryStatusAsString") val deliveryStatusAsString: String? = null,
    @SerialName("deliveryDateAsString") val deliveryDateAsString: String? = null,
    @SerialName("vipShopperManualPickup") val vipShopperManualPickup: Boolean? = null
)
