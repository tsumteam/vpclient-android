package ru.mercury.vpclient.features.main.intent

import ru.mercury.vpclient.core.mvi.Intent
import ru.mercury.vpclient.core.navigation.Route

sealed interface MainIntent: Intent {
    data class SelectTab(val route: Route): MainIntent
}
