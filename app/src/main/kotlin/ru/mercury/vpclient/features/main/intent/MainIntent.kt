package ru.mercury.vpclient.features.main.intent

import ru.mercury.vpclient.core.mvi.Intent
import androidx.navigation3.runtime.NavKey

sealed interface MainIntent: Intent {
    data class SelectTab(val route: NavKey): MainIntent
}
