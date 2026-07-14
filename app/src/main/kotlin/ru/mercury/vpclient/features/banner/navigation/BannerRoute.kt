package ru.mercury.vpclient.features.banner.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class BannerRoute(
    val url: String
): NavKey
