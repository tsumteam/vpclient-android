package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class EmployeeNotificationsRequest(
    @SerialName("filters") val filters: List<JsonElement> = emptyList()
)
