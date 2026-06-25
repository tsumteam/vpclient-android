package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyClientsClientBadgesResponse(
    @SerialName("clientId") val clientId: String? = null,
    @SerialName("basketIcon") val basketIcon: MyClientsIconWithBadgeResponse? = null,
    @SerialName("fittingIcon") val fittingIcon: MyClientsIconWithBadgeResponse? = null,
    @SerialName("messengerIcon") val messengerIcon: MyClientsIconWithBadgeResponse? = null,
    @SerialName("orderIcon") val orderIcon: MyClientsIconWithBadgeResponse? = null,
    @SerialName("compilationIcon") val compilationIcon: MyClientsIconWithBadgeResponse? = null
)
