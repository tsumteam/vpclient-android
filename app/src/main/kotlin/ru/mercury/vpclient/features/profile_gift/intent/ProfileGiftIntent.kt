package ru.mercury.vpclient.features.profile_gift.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface ProfileGiftIntent: Intent {
    data object BackClick: ProfileGiftIntent
}
