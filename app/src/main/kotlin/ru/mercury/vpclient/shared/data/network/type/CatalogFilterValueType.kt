package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class CatalogFilterValueType {
    @SerialName("bool") BOOL,
    @SerialName("brand") BRAND,
    @SerialName("catalogProductSize") CATALOG_PRODUCT_SIZE,
    @SerialName("dateTimeRange") DATE_TIME_RANGE,
    @SerialName("decimalRange") DECIMAL_RANGE,
    @SerialName("id") ID,
    @SerialName("idTree") ID_TREE,
    @SerialName("string") STRING
}
