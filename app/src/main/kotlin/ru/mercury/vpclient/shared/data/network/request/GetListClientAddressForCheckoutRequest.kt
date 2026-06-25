package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetListClientAddressForCheckoutRequest(
    @SerialName("pairedUserId") val pairedUserId: String? = null
)
