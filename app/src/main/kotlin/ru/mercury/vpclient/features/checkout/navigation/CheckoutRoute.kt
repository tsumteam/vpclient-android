package ru.mercury.vpclient.features.checkout.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.features.checkout.model.CheckoutSource

@Serializable
data class CheckoutRoute(
    val source: CheckoutSource = CheckoutSource.Fitting
): NavKey
