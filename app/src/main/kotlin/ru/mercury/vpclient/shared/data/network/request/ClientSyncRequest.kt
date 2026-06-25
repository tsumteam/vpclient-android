package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClientSyncRequest(
    @SerialName("allClients") val allClients: Boolean? = null
)
