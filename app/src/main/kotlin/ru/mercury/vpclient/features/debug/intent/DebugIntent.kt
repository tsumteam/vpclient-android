package ru.mercury.vpclient.features.debug.intent

import ru.mercury.vpclient.shared.mvi.Intent
import ru.mercury.vpclient.shared.data.network.env.ClientEnvironment

sealed interface DebugIntent: Intent {
    data object BackClick: DebugIntent
    data object FetchSettings: DebugIntent
    data object EnvironmentClick: DebugIntent
    data object DismissEnvironmentDialog: DebugIntent
    data object DropLocalDbClick: DebugIntent
    data class ToggleRequestDelay(val enabled: Boolean): DebugIntent
    data class SelectEnvironment(val environment: ClientEnvironment): DebugIntent
}
