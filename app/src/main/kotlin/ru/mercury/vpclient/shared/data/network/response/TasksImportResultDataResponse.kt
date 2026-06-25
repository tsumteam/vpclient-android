package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TasksImportResultDataResponse(
    @SerialName("employeeId") val employeeId: String? = null,
    @SerialName("clientId") val clientId: String? = null,
    @SerialName("crmId") val crmId: String? = null,
    @SerialName("message") val message: String? = null
)
