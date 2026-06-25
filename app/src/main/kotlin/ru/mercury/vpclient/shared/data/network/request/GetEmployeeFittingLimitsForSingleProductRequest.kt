package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.KittingType

@Serializable
data class GetEmployeeFittingLimitsForSingleProductRequest(
    @SerialName("itemId") val itemId: String? = null,
    @SerialName("colorId") val colorId: String? = null,
    @SerialName("sizeId") val sizeId: String? = null,
    @SerialName("kittingType") val kittingType: KittingType? = null
)
