package ru.mercury.vpclient.features.details

sealed interface DetailsMediaItem {
    data class Image(val url: String): DetailsMediaItem
    data class Video(val url: String): DetailsMediaItem
}
