package ru.mercury.vpclient.shared.data.entity

data class LoyaltyCardDescription(
    val id: Long = 0L,
    val type: LoyaltyCardType = LoyaltyCardType.Black,
    val cardName: String = "",
    val bonusRules: String = "",
    val termsForObtaining: String = "",
    val validity: String = "",
    val renewalTerms: String = "",
    val termsOfUse: String = ""
)
