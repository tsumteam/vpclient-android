package ru.mercury.vpclient.features.profile_order.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class ProfileOrderRoute(
    val orderNumber: String,
    val amount: String
): NavKey
