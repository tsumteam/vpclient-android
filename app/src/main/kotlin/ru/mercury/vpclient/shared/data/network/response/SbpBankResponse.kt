package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SbpBankResponse(
    @SerialName("bankName") val bankName: String? = null,
    @SerialName("logoURL") val logoUrl: String? = null,
    @SerialName("schema") val schema: String? = null,
    @SerialName("package_name") val packageName: String? = null
)
