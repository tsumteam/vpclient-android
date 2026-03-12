package ru.mercury.vpclient.features.main.tabs.profile.intent

import ru.mercury.vpclient.core.mvi.Intent

sealed interface ProfileIntent: Intent {
    data object Logout: ProfileIntent
}
