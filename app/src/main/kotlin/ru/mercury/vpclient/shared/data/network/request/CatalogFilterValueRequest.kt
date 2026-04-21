package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class CatalogFilterValueRequest(
    @SerialName("valueType") val valueType: String,
    @SerialName("value") val value: JsonElement
) {
    companion object {
        const val ID = "id"
        const val ID_TREE = "idTree"
        const val BRAND = "brand"
        const val CATALOG_PRODUCT_SIZE = "catalogProductSize"
        const val DECIMAL_RANGE = "decimalRange"
    }
}
