package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyClientsIconWithBadgeResponse(
    @SerialName("badge") val badge: Int? = null,
    @SerialName("icon") val icon: MyClientsIconResponse? = null
)
