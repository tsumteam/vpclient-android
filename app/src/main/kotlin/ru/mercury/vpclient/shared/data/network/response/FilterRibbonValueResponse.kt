package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class FilterRibbonValueResponse(
    @SerialName("label") val label: String?,
    @SerialName("value") val value: JsonElement?,
    @SerialName("ribbonSettings") val ribbonSettings: FilterRibbonSettingsResponse?
)
