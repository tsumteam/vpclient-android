package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FittingSyncRequest(
    @SerialName("allFittings") val allFittings: Boolean? = null
)
