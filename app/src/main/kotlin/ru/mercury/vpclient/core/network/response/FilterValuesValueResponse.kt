package ru.mercury.vpclient.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class FilterValuesValueResponse(
    @SerialName("label") val label: String?,
    @SerialName("value") val value: JsonElement?,
    @SerialName("labelPhotoUrl") val labelPhotoUrl: String? = null, // fixme
    @SerialName("labelItalian") val labelItalian: String? = null, // fixme
    @SerialName("labelFrench") val labelFrench: String? = null, // fixme
    @SerialName("labelInternational") val labelInternational: String? = null, // fixme
    @SerialName("order") val order: Int? = null, // fixme
    @SerialName("isFavorite") val isFavorite: Boolean? = null, // fixme
    @SerialName("isTopBrand") val isTopBrand: Boolean? = null // fixme
)
