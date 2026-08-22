package ru.mercury.vpclient.features.debug.model

import ru.mercury.vpclient.features.debug_env_dialog.model.DebugEnvModel
import ru.mercury.vpclient.shared.data.network.env.ClientEnvironment
import ru.mercury.vpclient.shared.mvi.Model

data class DebugModel(
    val userToken: String = "",
    val environment: ClientEnvironment = ClientEnvironment.TEST,
    val requestDelayEnabled: Boolean = false,
    val mockBackendEnabled: Boolean = false,
    val isEnvironmentDialogVisible: Boolean = false
): Model {

    val debugEnvState: DebugEnvModel
        get() = DebugEnvModel(
            selectedEnvironment = environment
        )
}
