package ru.mercury.vpclient.features.video.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface VideoIntent: Intent {
    data object BackClick: VideoIntent
    data object CollectVideo: VideoIntent
}
