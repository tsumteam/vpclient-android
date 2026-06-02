package ru.mercury.vpclient.features.fitting_info.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class FittingInfoRoute(
    val address: String,
    val deliveryDate: String
): NavKey
