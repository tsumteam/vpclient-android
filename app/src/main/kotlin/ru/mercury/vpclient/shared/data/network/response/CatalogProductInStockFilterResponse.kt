package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.FilterValueType

@Serializable
data class CatalogProductInStockFilterResponse(
    @SerialName("filterType") val filterType: String?,
    @SerialName("label") val label: String?,
    @SerialName("labelPhotoUrl") val labelPhotoUrl: String?,
    @SerialName("hint") val hint: String?,
    @SerialName("isMultiSelect") val isMultiSelect: Boolean?,
    @SerialName("order") val order: Int?,
    @SerialName("ribbonSettings") val ribbonSettings: FilterRibbonSettingsDtoResponse?,
    @SerialName("valuePickerViewSettings") val valuePickerViewSettings: FilterValuePickerViewSettingsDtoResponse?,
    @SerialName("valueType") val valueType: FilterValueType?,
    @SerialName("values") val values: List<FilterBoolValueResponse>
)
