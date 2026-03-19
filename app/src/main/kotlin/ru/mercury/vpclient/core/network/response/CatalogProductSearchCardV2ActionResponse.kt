package ru.mercury.vpclient.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CatalogProductSearchCardV2ActionResponse(
    @SerialName("name") val name: String?,
    @SerialName("isCashDesk") val isCashDesk: Boolean?,
    @SerialName("disclaimer") val disclaimer: String?,
    @SerialName("disclaimerPriority") val disclaimerPriority: Int?
)
