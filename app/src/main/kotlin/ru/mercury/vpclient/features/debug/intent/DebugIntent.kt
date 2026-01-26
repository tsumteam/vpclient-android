package ru.mercury.vpclient.features.debug.intent

import ru.mercury.vpclient.core.network.env.VPClientEnvironment
import ru.mercury.vpclient.core.mvi.Intent

sealed interface DebugIntent: Intent {
    data object BackClick: DebugIntent
    data object FetchSettings: DebugIntent
    data object EnvironmentClick: DebugIntent
    data object DismissEnvironmentDialog: DebugIntent
    data object DropLocalDbClick: DebugIntent
    data object AutofillClick: DebugIntent
    data object RequestDelayChangeFinished: DebugIntent
    data object NetworkRequest: DebugIntent
    data object DatabaseRequest: DebugIntent
    data class SelectEnvironment(val environment: VPClientEnvironment): DebugIntent
    data class RequestDelayChange(val value: Float): DebugIntent
}
