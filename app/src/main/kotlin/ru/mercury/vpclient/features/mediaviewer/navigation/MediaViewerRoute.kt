package ru.mercury.vpclient.features.mediaviewer.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
class MediaViewerRoute(
    val imageUrls: List<String>,
    val videoUrl: String?,
    val initialPage: Int
): NavKey
