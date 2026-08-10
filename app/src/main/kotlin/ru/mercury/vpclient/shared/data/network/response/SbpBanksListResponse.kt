package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SbpBanksListResponse(
    @SerialName("version") val version: String? = null,
    @SerialName("dictionary") val banks: List<SbpBankResponse>? = null
)
