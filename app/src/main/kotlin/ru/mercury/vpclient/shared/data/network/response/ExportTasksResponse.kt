package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExportTasksResponse(
    @SerialName("items") val items: List<ExportTasksResponseItemResponse>? = null
)
