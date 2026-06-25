package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.FilterValueType

@Serializable
data class FilterBoolValueResponse(
    @SerialName("valueType") val valueType: FilterValueType? = null,
    @SerialName("label") val label: String? = null,
    @SerialName("labelPhotoUrl") val labelPhotoUrl: String? = null,
    @SerialName("order") val order: Int? = null,
    @SerialName("ribbonSettings") val ribbonSettings: FilterRibbonSettingsDtoResponse? = null,
    @SerialName("value") val value: Boolean? = null
)
