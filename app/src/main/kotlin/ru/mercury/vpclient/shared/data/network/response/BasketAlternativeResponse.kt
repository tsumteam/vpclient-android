package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.BasketAlternativeType

@Serializable
data class BasketAlternativeResponse(
    @SerialName("product") val product: CatalogProductSearchCardResponse? = null,
    @SerialName("alternativeId") val alternativeId: String? = null,
    @SerialName("alternativeType") val alternativeType: BasketAlternativeType? = null
)
