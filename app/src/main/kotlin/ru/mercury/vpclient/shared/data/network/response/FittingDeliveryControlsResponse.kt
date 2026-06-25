package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FittingDeliveryControlsResponse(
    @SerialName("isEditDeliveryAvailable") val isEditDeliveryAvailable: Boolean? = null,
    @SerialName("isPaySwitchAvailable") val isPaySwitchAvailable: Boolean? = null,
    @SerialName("isExpirationDateExtensionAvailable") val isExpirationDateExtensionAvailable: Boolean? = null,
    @SerialName("isTransferFromStoreToHomeAvailable") val isTransferFromStoreToHomeAvailable: Boolean? = null,
    @SerialName("isConfirmDeliveryAvailable") val isConfirmDeliveryAvailable: Boolean? = null,
    @SerialName("isRequiredToConfirmDeliverySignActive") val isRequiredToConfirmDeliverySignActive: Boolean? = null
)
