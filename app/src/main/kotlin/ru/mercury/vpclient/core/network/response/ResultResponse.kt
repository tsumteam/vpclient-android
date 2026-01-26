package ru.mercury.vpclient.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResultResponse(
    @SerialName("result") val result: String?
) {
    companion object {
        const val RESULT_OK = "OK"
    }
}
