package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.ViewHistoryItemType

@Serializable
data class ViewHistoryItemResponse(
    @SerialName("product") val product: CatalogProductSearchCardResponse? = null,
    @SerialName("timestamp") val timestamp: String? = null,
    @SerialName("timestampAsAString") val timestampAsAString: String? = null,
    @SerialName("type") val type: ViewHistoryItemType? = null
)
