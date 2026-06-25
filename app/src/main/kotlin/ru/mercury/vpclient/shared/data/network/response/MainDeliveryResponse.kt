package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MainDeliveryResponse(
    @SerialName("boutiques") val boutiques: List<BoutiqueResponse>? = null,
    @SerialName("buttonTitle") val buttonTitle: String? = null,
    @SerialName("callCenterEmail") val callCenterEmail: String? = null,
    @SerialName("callCenterPhone") val callCenterPhone: String? = null,
    @SerialName("catalogueText") val catalogueText: String? = null,
    @SerialName("header") val header: String? = null,
    @SerialName("subheader") val subheader: String? = null,
    @SerialName("urlRegulations") val urlRegulations: String? = null,
    @SerialName("vipPhone") val vipPhone: String? = null
)
