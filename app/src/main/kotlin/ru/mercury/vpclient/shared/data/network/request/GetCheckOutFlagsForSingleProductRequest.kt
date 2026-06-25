package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.FittingType

@Serializable
data class GetCheckOutFlagsForSingleProductRequest(
    @SerialName("pairedUserId") val pairedUserId: String? = null,
    @SerialName("address") val address: String? = null,
    @SerialName("comment") val comment: String? = null,
    @SerialName("itemId") val itemId: String? = null,
    @SerialName("colorId") val colorId: String? = null,
    @SerialName("sizeId") val sizeId: String? = null,
    @SerialName("quantity") val quantity: Int? = null,
    @SerialName("fittingType") val fittingType: FittingType? = null
)
