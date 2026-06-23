package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class FilterValuesValueResponse(
    @SerialName("label") val label: String?,
    @SerialName("value") val value: JsonElement?,
    @SerialName("labelPhotoUrl") val labelPhotoUrl: String? = null,
    @SerialName("labelItalian") val labelItalian: String? = null,
    @SerialName("labelFrench") val labelFrench: String? = null,
    @SerialName("labelInternational") val labelInternational: String? = null,
    @SerialName("order") val order: Int? = null,
    @SerialName("isFavorite") val isFavorite: Boolean? = null,
    @SerialName("isTopBrand") val isTopBrand: Boolean? = null
)
