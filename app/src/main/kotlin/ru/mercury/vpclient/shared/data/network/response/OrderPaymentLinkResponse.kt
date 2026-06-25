package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderPaymentLinkResponse(
    @SerialName("urlPayment") val urlPayment: String? = null
)
