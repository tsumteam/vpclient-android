@file:OptIn(ExperimentalSerializationApi::class)

package ru.mercury.vpclient.shared.network.request

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CatalogFilterRequest(
    @SerialName("filterType") val filterType: String,
    @SerialName("filterSubtype") val filterSubtype: String?,
    @SerialName("values") @EncodeDefault val values: List<CatalogFilterValueRequest>
) {
    companion object {
        const val ACTION = "action"
        const val ATTRIBUTE = "attribute"
        const val CATEGORY = "category"
        const val BRAND = "brand"
        const val COLOR = "color"
        const val SIZE = "size"
    }
}
