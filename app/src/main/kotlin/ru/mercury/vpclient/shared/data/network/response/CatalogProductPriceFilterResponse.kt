package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.FilterValueType

@Serializable
data class CatalogProductPriceFilterResponse(
    @SerialName("filterType") val filterType: String? = null,
    @SerialName("label") val label: String? = null,
    @SerialName("labelPhotoUrl") val labelPhotoUrl: String? = null,
    @SerialName("hint") val hint: String? = null,
    @SerialName("isMultiSelect") val isMultiSelect: Boolean? = null,
    @SerialName("order") val order: Int? = null,
    @SerialName("ribbonSettings") val ribbonSettings: FilterRibbonSettingsDtoResponse? = null,
    @SerialName("valuePickerViewSettings") val valuePickerViewSettings: FilterValuePickerViewSettingsDtoResponse? = null,
    @SerialName("valueType") val valueType: FilterValueType? = null,
    @SerialName("values") val values: List<FilterDecimalRangeValueResponse> = emptyList()
)
