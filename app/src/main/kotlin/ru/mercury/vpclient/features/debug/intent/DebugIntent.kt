package ru.mercury.vpclient.features.debug.intent

import ru.mercury.vpclient.core.mvi.Intent
import ru.mercury.vpclient.core.network.env.VPClientEnvironment

sealed interface DebugIntent: Intent {
    data object BackClick: DebugIntent
    data object FetchSettings: DebugIntent
    data object EnvironmentClick: DebugIntent
    data object DismissEnvironmentDialog: DebugIntent
    data object DropLocalDbClick: DebugIntent
    data class SelectEnvironment(val environment: VPClientEnvironment): DebugIntent
}
