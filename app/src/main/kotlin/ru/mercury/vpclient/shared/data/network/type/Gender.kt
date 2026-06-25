package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Gender {
    @SerialName("none") NONE,
    @SerialName("masculine") MASCULINE,
    @SerialName("feminine") FEMININE
}
