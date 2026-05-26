package ru.mercury.vpclient.features.fitting_confirmation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class FittingConfirmationRoute(
    val productIds: List<String>
): NavKey
