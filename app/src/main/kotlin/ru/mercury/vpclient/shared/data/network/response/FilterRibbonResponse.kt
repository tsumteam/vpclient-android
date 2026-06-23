package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.CatalogFilterValueType

@Serializable
data class FilterRibbonResponse(
    @SerialName("filterType") val filterType: String?,
    @SerialName("filterSubtype") val filterSubtype: String?,
    @SerialName("label") val label: String?,
    @SerialName("hint") val hint: String? = null,
    @SerialName("isMultiSelect") val isMultiSelect: Boolean? = null,
    @SerialName("order") val order: Int?,
    @SerialName("valueType") val valueType: CatalogFilterValueType? = null,
    @SerialName("valuePickerViewSettings") val valuePickerViewSettings: FilterValuePickerViewSettingsResponse? = null,
    @SerialName("ribbonSettings") val ribbonSettings: FilterRibbonSettingsResponse?,
    @SerialName("values") val values: List<FilterRibbonValueResponse>?
)
