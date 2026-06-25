package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetailedStocksResponseRowControlsResponse(
    @SerialName("isGetReserveButtonAvailable") val isGetReserveButtonAvailable: Boolean? = null,
    @SerialName("isGetTransferButtonAvailable") val isGetTransferButtonAvailable: Boolean? = null,
    @SerialName("isAddToBasketButtonAvailable") val isAddToBasketButtonAvailable: Boolean? = null,
    @SerialName("isSubscribeButtonAvailable") val isSubscribeButtonAvailable: Boolean? = null
)
