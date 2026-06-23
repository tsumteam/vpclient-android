package ru.mercury.vpclient.features.main.intent

import androidx.navigation3.runtime.NavKey
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface MainIntent: Intent {
    data object CollectActivityCounters: MainIntent
    data object LoadActivityCounters: MainIntent
    data class SelectTab(val route: NavKey): MainIntent
}
