package ru.mercury.vpclient.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FilterRibbonResponse(
    @SerialName("filterType") val filterType: String?,
    @SerialName("filterSubtype") val filterSubtype: String?,
    @SerialName("label") val label: String?,
    @SerialName("order") val order: Int?,
    @SerialName("ribbonSettings") val ribbonSettings: FilterRibbonSettingsResponse?,
    @SerialName("values") val values: List<FilterRibbonValueResponse>?
)
