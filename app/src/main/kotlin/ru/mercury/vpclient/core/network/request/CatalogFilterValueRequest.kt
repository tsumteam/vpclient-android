package ru.mercury.vpclient.core.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CatalogFilterValueRequest(
    @SerialName("valueType") val valueType: String,
    @SerialName("value") val value: Int
) {
    companion object {
        const val ID = "id"
        const val ID_TREE = "idTree"
        const val BRAND = "brand"
        const val CATALOG_PRODUCT_SIZE = "catalogProductSize"
    }
}
