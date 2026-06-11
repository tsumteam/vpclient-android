package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoyaltyLinkByPhoneRequest(
    @SerialName("clientPhone") val clientPhone: String
)
