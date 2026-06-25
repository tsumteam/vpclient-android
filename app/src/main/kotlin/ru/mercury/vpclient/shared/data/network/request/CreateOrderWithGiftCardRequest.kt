package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.GiftCardType
import ru.mercury.vpclient.shared.data.network.type.PaymentType

@Serializable
data class CreateOrderWithGiftCardRequest(
    @SerialName("pairedUserId") val pairedUserId: String? = null,
    @SerialName("templateId") val templateId: Int? = null,
    @SerialName("itemId") val itemId: String? = null,
    @SerialName("amount") val amount: Double? = null,
    @SerialName("type") val type: GiftCardType? = null,
    @SerialName("email") val email: String? = null,
    @SerialName("phone") val phone: String? = null,
    @SerialName("dateFrom") val dateFrom: String? = null,
    @SerialName("dateTo") val dateTo: String? = null,
    @SerialName("paymentType") val paymentType: PaymentType? = null
)
