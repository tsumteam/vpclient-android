package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.DateReceiptExpiredStatus
import ru.mercury.vpclient.shared.data.network.type.OrderLogisticStatusRequest

@Serializable
data class OrderProductResponse(
    @SerialName("lineId") val lineId: String? = null,
    @SerialName("logisticStatus") val logisticStatus: OrderLogisticStatusRequest? = null,
    @SerialName("logisticStatusAsStringForClient") val logisticStatusAsStringForClient: String? = null,
    @SerialName("logisticStatusAsStringForEmployee") val logisticStatusAsStringForEmployee: String? = null,
    @SerialName("price") val price: Double? = null,
    @SerialName("product") val product: CatalogProductSearchCardResponse? = null,
    @SerialName("dateReceiptAsString") val dateReceiptAsString: String? = null,
    @SerialName("dateReceiptExpiredStatus") val dateReceiptExpiredStatus: DateReceiptExpiredStatus? = null,
    @SerialName("giftCardTemplateId") val giftCardTemplateId: Int? = null,
    @SerialName("giftCardEmailReceiver") val giftCardEmailReceiver: String? = null,
    @SerialName("giftCardPhoneReceiver") val giftCardPhoneReceiver: String? = null,
    @SerialName("giftCardSendDateTime") val giftCardSendDateTime: String? = null,
    @SerialName("isOrderVirtualGiftCard") val isOrderVirtualGiftCard: Boolean? = null
)
