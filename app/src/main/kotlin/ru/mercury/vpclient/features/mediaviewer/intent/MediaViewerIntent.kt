package ru.mercury.vpclient.features.mediaviewer.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface MediaViewerIntent: Intent {
    data object BackClick: MediaViewerIntent
}
