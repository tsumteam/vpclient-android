package ru.mercury.vpclient.features.media.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class MediaRoute(
    val imageUrls: List<String>,
    val videoUrl: String?,
    val initialPage: Int
): NavKey
