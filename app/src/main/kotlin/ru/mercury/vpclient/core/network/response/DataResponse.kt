package ru.mercury.vpclient.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DataResponse(
    @SerialName("data") val data: String?
) {
    companion object Companion {
        const val RESULT_OK = "ok"
    }
}
