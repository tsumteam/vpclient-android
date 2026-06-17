package ru.mercury.vpclient.features.media.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface MediaIntent: Intent {
    data object BackClick: MediaIntent
    data object CollectMedia: MediaIntent
}
