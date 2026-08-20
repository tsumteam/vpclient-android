package ru.mercury.vpclient.features.debug_env_dialog.intent

import ru.mercury.vpclient.shared.data.network.env.ClientEnvironment
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface DebugEnvIntent: Intent {
    data object DismissRequest: DebugEnvIntent
    data class SelectEnvironment(val environment: ClientEnvironment): DebugEnvIntent
}
