package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class CompilationStatus {
    @SerialName("none") NONE,
    @SerialName("active") ACTIVE,
    @SerialName("opened") OPENED,
    @SerialName("sent") SENT,
    @SerialName("archived") ARCHIVED,
    @SerialName("notSent") NOT_SENT
}
