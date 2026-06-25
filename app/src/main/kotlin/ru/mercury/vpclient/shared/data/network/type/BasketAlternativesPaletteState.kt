package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class BasketAlternativesPaletteState {
    @SerialName("open") OPEN,
    @SerialName("hidden") HIDDEN
}
