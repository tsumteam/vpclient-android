package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.response.DeliveryTimeResponse
import ru.mercury.vpclient.shared.data.network.type.PaymentType
import ru.mercury.vpclient.shared.data.network.type.FittingType
import ru.mercury.vpclient.shared.data.network.type.DeliveryType

@Serializable
data class OrderCreationRequest(
    @SerialName("pairedUserId") val pairedUserId: String? = null,
    @SerialName("paymentType") val paymentType: PaymentType? = null,
    @SerialName("deliveryTime") val deliveryTime: DeliveryTimeResponse? = null,
    @SerialName("fittingType") val fittingType: FittingType? = null,
    @SerialName("deliveryType") val deliveryType: DeliveryType? = null,
    @SerialName("latitude") val latitude: Double? = null,
    @SerialName("longitude") val longitude: Double? = null,
    @SerialName("address") val address: String? = null,
    @SerialName("addressComment") val addressComment: String? = null,
    @SerialName("packOrderMyself") val packOrderMyself: Boolean? = null,
    @SerialName("manageClientCommunicationsMyself") val manageClientCommunicationsMyself: Boolean? = null,
    @SerialName("deliveryComment") val deliveryComment: String? = null
)
