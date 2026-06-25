package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessengerActivityReportResponseResponse(
    @SerialName("error") val error: ErrorResponse? = null,
    @SerialName("data") val data: MessengerActivityReportResponse? = null
)
