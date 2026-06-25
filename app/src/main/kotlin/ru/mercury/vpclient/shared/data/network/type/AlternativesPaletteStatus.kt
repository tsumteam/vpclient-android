package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class AlternativesPaletteStatus {
    @SerialName("open") OPEN,
    @SerialName("hidden") HIDDEN
}
