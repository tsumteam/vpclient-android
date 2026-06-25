package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FilterRibbonSettingsDtoResponse(
    @SerialName("isVisible") val isVisible: Boolean? = null,
    @SerialName("row") val row: Int? = null,
    @SerialName("order") val order: Int? = null
)
