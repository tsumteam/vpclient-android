package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.ProductAction

@Serializable
data class LookProductsResponseItemResponse(
    @SerialName("searchCard") val searchCard: CatalogProductSearchCardResponse? = null,
    @SerialName("action") val action: LookActionInfoResponse? = null,
    @SerialName("lastStatus") val lastStatus: ProductAction? = null
)
