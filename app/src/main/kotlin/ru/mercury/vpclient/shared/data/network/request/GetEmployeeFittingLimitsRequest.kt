package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.KittingType

@Serializable
data class GetEmployeeFittingLimitsRequest(
    @SerialName("clientId") val clientId: String? = null,
    @SerialName("basketLineIds") val basketLineIds: List<String>? = null,
    @SerialName("kittingType") val kittingType: KittingType? = null
)
