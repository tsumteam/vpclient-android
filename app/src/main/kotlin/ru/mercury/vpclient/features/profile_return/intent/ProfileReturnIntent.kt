package ru.mercury.vpclient.features.profile_return.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface ProfileReturnIntent: Intent {
    data object BackClick: ProfileReturnIntent
}
