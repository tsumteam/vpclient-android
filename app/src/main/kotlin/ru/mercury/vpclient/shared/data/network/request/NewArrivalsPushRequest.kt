package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.CatalogItemType

@Serializable
data class NewArrivalsPushRequest(
    @SerialName("pushMessage") val pushMessage: String? = null,
    @SerialName("pushTitle") val pushTitle: String? = null,
    @SerialName("clientIds") val clientIds: List<String>? = null,
    @SerialName("employeeIds") val employeeIds: List<String>? = null,
    @SerialName("category") val category: CatalogItemType? = null
)
