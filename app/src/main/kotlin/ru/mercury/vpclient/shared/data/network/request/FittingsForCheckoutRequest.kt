package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.CheckoutBonusType

@Serializable
data class FittingsForCheckoutRequest(
    @SerialName("bonusType") val bonusType: CheckoutBonusType,
    @SerialName("giftCertificateNumber") val giftCertificateNumber: String? = null,
    @SerialName("ignoreCashDeskActions") val ignoreCashDeskActions: Boolean? = null
)
