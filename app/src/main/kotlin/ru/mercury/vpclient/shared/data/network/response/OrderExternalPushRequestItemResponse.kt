package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.ExternalPushReceiver

@Serializable
data class OrderExternalPushRequestItemResponse(
    @SerialName("orderId") val orderId: String? = null,
    @SerialName("phone") val phone: String? = null,
    @SerialName("pushMessage") val pushMessage: String? = null,
    @SerialName("pushTitle") val pushTitle: String? = null,
    @SerialName("messageId") val messageId: String? = null,
    @SerialName("clientPhone") val clientPhone: String? = null,
    @SerialName("pushReceiver") val pushReceiver: ExternalPushReceiver? = null
)
