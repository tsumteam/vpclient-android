package ru.mercury.vpclient.features.video.model

import ru.mercury.vpclient.shared.mvi.Model

data class VideoModel(
    val videoUrl: String = ""
): Model {

    companion object {
        const val VIDEO_CONTROLS_HIDE_DELAY_MILLIS = 2_000L
        const val VIDEO_POSITION_UPDATE_DELAY_MILLIS = 250L
    }
}
