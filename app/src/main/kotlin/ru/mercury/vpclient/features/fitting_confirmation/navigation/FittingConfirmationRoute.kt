package ru.mercury.vpclient.features.fitting_confirmation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.entity.FittingTypeDtoEnum

@Serializable
data class FittingConfirmationRoute(
    val productIds: List<String>,
    val deliveryId: String? = null,
    val fittingType: FittingTypeDtoEnum? = null
): NavKey
