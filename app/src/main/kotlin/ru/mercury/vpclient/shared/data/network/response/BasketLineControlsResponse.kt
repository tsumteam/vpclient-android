package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.AlternativesPaletteStatus

@Serializable
data class BasketLineControlsResponse(
    @SerialName("isChooseAlternativeFromCatalogAvailable") val isChooseAlternativeFromCatalogAvailable: Boolean? = null,
    @SerialName("alternativesPalette") val alternativesPalette: AlternativesPaletteStatus? = null,
    @SerialName("isAlternativePaletteControlsAvailable") val isAlternativePaletteControlsAvailable: Boolean? = null,
    @SerialName("isManualAddAlternativeAvailable") val isManualAddAlternativeAvailable: Boolean? = null,
    @SerialName("isSwitchAlternativeBackToOriginalAvailable") val isSwitchAlternativeBackToOriginalAvailable: Boolean? = null,
    @SerialName("isChangingLocationAvailable") val isChangingLocationAvailable: Boolean? = null
)
