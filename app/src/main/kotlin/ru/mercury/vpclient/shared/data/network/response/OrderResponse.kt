package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.OrderPaymentStatus
import ru.mercury.vpclient.shared.data.network.type.PaymentTypeRequest

@Serializable
data class OrderResponse(
    @SerialName("clientEmail") val clientEmail: String? = null,
    @SerialName("clientId") val clientId: String? = null,
    @SerialName("clientName") val clientName: String? = null,
    @SerialName("clientPhone") val clientPhone: String? = null,
    @SerialName("clientSurname") val clientSurname: String? = null,
    @SerialName("creationDate") val creationDate: String? = null,
    @SerialName("id") val id: String? = null,
    @SerialName("isFinished") val isFinished: Boolean? = null,
    @SerialName("isDelivered") val isDelivered: Boolean? = null,
    @SerialName("paymentStatus") val paymentStatus: OrderPaymentStatus? = null,
    @SerialName("paymentStatusAsString") val paymentStatusAsString: String? = null,
    @SerialName("reservedBonusAmount") val reservedBonusAmount: Double? = null,
    @SerialName("bonusAmount") val bonusAmount: Double? = null,
    @SerialName("isFinishedAsString") val isFinishedAsString: String? = null,
    @SerialName("order") val order: Int? = null,
    @SerialName("orderNumber") val orderNumber: String? = null,
    @SerialName("paymentType") val paymentType: PaymentTypeRequest? = null,
    @SerialName("deliveries") val deliveries: List<OrderDeliveryResponse>? = null,
    @SerialName("totalPrice") val totalPrice: Double? = null,
    @SerialName("controls") val controls: OrderResponseControlsResponse? = null,
    @SerialName("isOrderWithGiftCard") val isOrderWithGiftCard: Boolean? = null
)
