package ru.mercury.vpclient.features.profile_loyalty_terms.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class ProfileLoyaltyTermsRoute(
    val url: String
): NavKey
