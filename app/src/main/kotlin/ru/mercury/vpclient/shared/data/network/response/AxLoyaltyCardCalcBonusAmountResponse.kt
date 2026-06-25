package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AxLoyaltyCardCalcBonusAmountResponse(
    @SerialName("confirmedAmount") val confirmedAmount: Double? = null,
    @SerialName("paymentAmount") val paymentAmount: Double? = null
)
