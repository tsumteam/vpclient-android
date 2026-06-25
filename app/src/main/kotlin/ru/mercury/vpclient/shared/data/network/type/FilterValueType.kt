package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class FilterValueType {
    @SerialName("string") STRING,
    @SerialName("dateTimeRange") DATE_TIME_RANGE,
    @SerialName("decimalRange") DECIMAL_RANGE,
    @SerialName("id") ID,
    @SerialName("idTree") ID_TREE,
    @SerialName("bool") BOOL,
    @SerialName("catalogProductSize") CATALOG_PRODUCT_SIZE
}
