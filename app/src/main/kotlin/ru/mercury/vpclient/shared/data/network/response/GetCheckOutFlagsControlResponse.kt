package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetCheckOutFlagsControlResponse(
    @SerialName("leaveItemsToClientCaption") val leaveItemsToClientCaption: String? = null,
    @SerialName("isDeliverySettingsAvailable") val isDeliverySettingsAvailable: Boolean? = null,
    @SerialName("isKittingSettingsAvailable") val isKittingSettingsAvailable: Boolean? = null,
    @SerialName("isKittingByEmployeeAvailable") val isKittingByEmployeeAvailable: Boolean? = null,
    @SerialName("isDeliveryByEmployeeAvailable") val isDeliveryByEmployeeAvailable: Boolean? = null,
    @SerialName("isLeaveItemsToClientAvailable") val isLeaveItemsToClientAvailable: Boolean? = null,
    @SerialName("isClientInHouseSwitchAvailable") val isClientInHouseSwitchAvailable: Boolean? = null,
    @SerialName("isVipShopperManualPickupAvailable") val isVipShopperManualPickupAvailable: Boolean? = null
)
