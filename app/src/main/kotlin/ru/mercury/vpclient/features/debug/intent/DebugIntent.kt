package ru.mercury.vpclient.features.debug.intent

import ru.mercury.vpclient.features.debug_env_dialog.intent.DebugEnvIntent
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface DebugIntent: Intent {
    data object BackClick: DebugIntent
    data object FetchSettings: DebugIntent
    data object EnvironmentClick: DebugIntent
    data object DropLocalDbClick: DebugIntent
    data object ToggleMockBackend: DebugIntent
    data class ToggleRequestDelay(val enabled: Boolean): DebugIntent
    data class OnDebugEnvIntent(val intent: DebugEnvIntent): DebugIntent
}
