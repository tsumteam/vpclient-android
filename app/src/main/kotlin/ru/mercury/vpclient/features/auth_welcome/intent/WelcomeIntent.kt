package ru.mercury.vpclient.features.auth_welcome.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface WelcomeIntent: Intent {
    data object RegisterClick: WelcomeIntent
    data object LoginClick: WelcomeIntent
}
