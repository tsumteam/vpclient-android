package ru.mercury.vpclient.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentSbpStatusResponse(
    @SerialName("status") val status: String?,
    @SerialName("trxId") val trxId: String?
) {
    companion object {
        const val STATUS_INPROGRESS = "inProgress"
        const val STATUS_COMPLETED = "completed"
        const val STATUS_DECLINED = "declined"
        const val STATUS_EXPIRED = "expired"
    }
}
