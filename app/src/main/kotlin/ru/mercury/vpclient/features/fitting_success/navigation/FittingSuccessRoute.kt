package ru.mercury.vpclient.features.fitting_success.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class FittingSuccessRoute(
    val deliveryLines: List<FittingSuccessDeliveryLine> = emptyList(),
    val address: String = ""
): NavKey

@Serializable
data class FittingSuccessDeliveryLine(
    val intervalSummary: String,
    val productsCount: Int
)
