package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.KittingType

@Serializable
data class CheckOutKittingSettingsResponse(
    @SerialName("kittingType") val kittingType: KittingType? = null
)
