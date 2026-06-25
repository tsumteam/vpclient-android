package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CardTypeResponse(
    @SerialName("id") val id: Long? = null,
    @SerialName("type") val type: String? = null,
    @SerialName("cardName") val cardName: String? = null,
    @SerialName("bonusRules") val bonusRules: String? = null,
    @SerialName("termsForObtaining") val termsForObtaining: String? = null,
    @SerialName("validity") val validity: String? = null,
    @SerialName("renewalTerms") val renewalTerms: String? = null,
    @SerialName("termsOfUse") val termsOfUse: String? = null
)
