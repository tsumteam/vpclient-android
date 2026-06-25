package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class BasketAlternativeType {
    @SerialName("manual") MANUAL,
    @SerialName("original") ORIGINAL,
    @SerialName("automatic") AUTOMATIC
}
