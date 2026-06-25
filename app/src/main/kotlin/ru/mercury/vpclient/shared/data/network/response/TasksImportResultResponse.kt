package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TasksImportResultResponse(
    @SerialName("errors") val errors: List<TasksImportResultDataResponse>? = null
)
