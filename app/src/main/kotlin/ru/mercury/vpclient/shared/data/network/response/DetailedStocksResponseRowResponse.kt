package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.System

@Serializable
data class DetailedStocksResponseRowResponse(
    @SerialName("itemId") val itemId: String? = null,
    @SerialName("locationId") val locationId: String? = null,
    @SerialName("locationString") val locationString: String? = null,
    @SerialName("siteId") val siteId: String? = null,
    @SerialName("site") val site: String? = null,
    @SerialName("sizeId") val sizeId: String? = null,
    @SerialName("totalRemains") val totalRemains: Int? = null,
    @SerialName("availableRemains") val availableRemains: Int? = null,
    @SerialName("serialId") val serialId: String? = null,
    @SerialName("moratoryEndDate") val moratoryEndDate: String? = null,
    @SerialName("system") val system: System? = null,
    @SerialName("controls") val controls: DetailedStocksResponseRowControlsResponse? = null,
    @SerialName("price") val price: Double? = null
)
