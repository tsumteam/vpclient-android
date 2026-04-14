package ru.mercury.vpclient.shared.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DataResponse(
    @SerialName("data") val data: String?
) {
    companion object {
        const val RESULT_OK = "ok"
    }
}
