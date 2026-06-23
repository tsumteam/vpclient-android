package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import ru.mercury.vpclient.shared.data.network.type.CatalogFilterValueType

@Serializable
data class CatalogFilterValueRequest(
    @SerialName("valueType") val valueType: CatalogFilterValueType,
    @SerialName("value") val value: JsonElement
)
