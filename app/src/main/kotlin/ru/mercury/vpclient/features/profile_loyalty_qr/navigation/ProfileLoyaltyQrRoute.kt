package ru.mercury.vpclient.features.profile_loyalty_qr.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class ProfileLoyaltyQrRoute(
    val qrCode: String
): NavKey
