package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FilterRibbonSettingsResponse(
    @SerialName("isVisible") val isVisible: Boolean?,
    @SerialName("row") val row: Int?,
    @SerialName("order") val order: Int?
)
