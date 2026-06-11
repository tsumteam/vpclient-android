@file:OptIn(ExperimentalSerializationApi::class)

package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileOrdersSalesRequest(
    @SerialName("clientId") val clientId: String,
    @SerialName("limit") val limit: Int,
    @SerialName("offset") val offset: Int,
    @SerialName("filters") @EncodeDefault val filters: List<CatalogFilterRequest> = emptyList()
)
