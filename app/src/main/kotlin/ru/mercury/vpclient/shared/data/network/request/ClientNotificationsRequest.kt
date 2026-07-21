@file:OptIn(ExperimentalSerializationApi::class)

package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class ClientNotificationsRequest(
    @SerialName("filters") @EncodeDefault val filters: List<JsonObject> = emptyList()
)
