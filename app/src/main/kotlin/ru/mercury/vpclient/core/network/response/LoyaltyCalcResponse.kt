package ru.mercury.vpclient.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoyaltyCalcResponse(
    @SerialName("confirmedAmount") val confirmedAmount: Double?,
    @SerialName("paymentAmount") val paymentAmount: Double? // сумма корзины после списания бонусов
) {
    companion object {
        const val EMPTY_BALANCE_ERROR_CODE = 88
    }
}
