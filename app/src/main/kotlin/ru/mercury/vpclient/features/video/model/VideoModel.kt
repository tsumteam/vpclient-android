package ru.mercury.vpclient.features.video.model

import ru.mercury.vpclient.shared.mvi.Model

data class VideoModel(
    val videoUrl: String = ""
): Model
