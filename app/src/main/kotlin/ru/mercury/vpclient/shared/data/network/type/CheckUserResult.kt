package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class CheckUserResult {
    @SerialName("exists") EXISTS,
    @SerialName("notExists") NOT_EXISTS,
    @SerialName("invalidPhone") INVALID_PHONE
}
