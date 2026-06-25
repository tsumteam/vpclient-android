package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.response.TransferBasketToFittingLineResponse
import ru.mercury.vpclient.shared.data.network.type.FittingType
import ru.mercury.vpclient.shared.data.network.type.DeliveryType
import ru.mercury.vpclient.shared.data.network.type.KittingType

@Serializable
data class TransferBasketToFittingRequest(
    @SerialName("pairedUserId") val pairedUserId: String? = null,
    @SerialName("lines") val lines: List<TransferBasketToFittingLineResponse> = emptyList(),
    @SerialName("latitude") val latitude: Double? = null,
    @SerialName("longitude") val longitude: Double? = null,
    @SerialName("address") val address: String? = null,
    @SerialName("addressComment") val addressComment: String? = null,
    @SerialName("fittingType") val fittingType: FittingType? = null,
    @SerialName("deliveryType") val deliveryType: DeliveryType? = null,
    @SerialName("kittingType") val kittingType: KittingType? = null,
    @SerialName("leaveItemsToClient") val leaveItemsToClient: Boolean? = null,
    @SerialName("isGoWithCourier") val isGoWithCourier: Boolean? = null,
    @SerialName("packOrderMyself") val packOrderMyself: Boolean? = null,
    @SerialName("manageClientCommunicationsMyself") val manageClientCommunicationsMyself: Boolean? = null,
    @SerialName("deliveryComment") val deliveryComment: String? = null,
    @SerialName("isClientInHouse") val isClientInHouse: Boolean? = null,
    @SerialName("vipShopperManualPickup") val vipShopperManualPickup: Boolean? = null,
    @SerialName("tag") val tag: String? = null
)
