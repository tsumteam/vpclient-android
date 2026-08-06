package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class CheckoutBonusType {
    @SerialName("withoutBonus") WITHOUT_BONUS,
    @SerialName("loyaltyCard") LOYALTY_CARD,
    @SerialName("giftCertificate") GIFT_CERTIFICATE
}
