package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.FilterValueType

@Serializable
data class CatalogProductFilterSizeValueResponse(
    @SerialName("valueType") val valueType: FilterValueType? = null,
    @SerialName("label") val label: String? = null,
    @SerialName("labelPhotoUrl") val labelPhotoUrl: String? = null,
    @SerialName("order") val order: Int? = null,
    @SerialName("ribbonSettings") val ribbonSettings: FilterRibbonSettingsDtoResponse? = null,
    @SerialName("value") val value: Int? = null,
    @SerialName("labelInternational") val labelInternational: String? = null,
    @SerialName("labelItalian") val labelItalian: String? = null,
    @SerialName("labelFrench") val labelFrench: String? = null,
    @SerialName("labelInches") val labelInches: String? = null
)
