package ru.mercury.vpclient.features.mediaviewer.model

import ru.mercury.vpclient.shared.data.entity.DetailsMediaItem
import ru.mercury.vpclient.shared.mvi.Model

data class MediaViewerModel(
    val imageUrls: List<String> = emptyList(),
    val videoUrl: String? = null,
    val initialPage: Int = 0
): Model {

    val mediaItems: List<DetailsMediaItem>
        get() {
            val images = imageUrls.map { DetailsMediaItem.Image(it) }
            val video = videoUrl?.let { listOf(DetailsMediaItem.Video(it)) }.orEmpty()
            return images + video
        }

    val hasVideo: Boolean
        get() {
            return videoUrl != null
        }
}
