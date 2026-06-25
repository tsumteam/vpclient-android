package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderDeliveryResponseControlsResponse(
    @SerialName("isEditDeliveryAvailable") val isEditDeliveryAvailable: Boolean? = null
)
