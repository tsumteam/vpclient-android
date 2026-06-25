package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.GiftCardType

@Serializable
data class GiftCardResponseItemResponse(
    @SerialName("id") val id: Int? = null,
    @SerialName("itemId") val itemId: String? = null,
    @SerialName("type") val type: GiftCardType? = null,
    @SerialName("maxAmount") val maxAmount: Double? = null,
    @SerialName("minAmount") val minAmount: Double? = null,
    @SerialName("defaultAmount") val defaultAmount: Double? = null,
    @SerialName("presetAmounts") val presetAmounts: List<Double>? = null,
    @SerialName("templates") val templates: List<GiftCardTemplateResponseItemResponse>? = null
)
