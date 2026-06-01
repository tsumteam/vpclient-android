package ru.mercury.vpclient.features.debug_env_dialog.intent

import ru.mercury.vpclient.shared.data.network.env.ClientEnvironment
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface DebugEnvironmentDialogIntent: Intent {
    data object DismissRequest: DebugEnvironmentDialogIntent
    data class SelectEnvironment(val environment: ClientEnvironment): DebugEnvironmentDialogIntent
}
