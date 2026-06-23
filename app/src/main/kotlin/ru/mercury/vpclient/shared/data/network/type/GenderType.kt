package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class GenderType {
    @SerialName("none") NONE,
    @SerialName("masculine") MASCULINE,
    @SerialName("feminine") FEMININE
}
