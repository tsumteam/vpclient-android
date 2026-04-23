package ru.mercury.vpclient.shared.data.entity

sealed interface DetailsMediaItem {
    data class Image(val url: String): DetailsMediaItem
    data class Video(val url: String): DetailsMediaItem
}