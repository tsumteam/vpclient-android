package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExportEmployeeEventsResponse(
    @SerialName("items") val items: List<ExportEmployeeEventsResponseItemResponse>? = null
)
