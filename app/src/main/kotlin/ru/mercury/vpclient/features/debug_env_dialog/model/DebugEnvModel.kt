package ru.mercury.vpclient.features.debug_env_dialog.model

import ru.mercury.vpclient.shared.data.network.env.ClientEnvironment

data class DebugEnvModel(
    val selectedEnvironment: ClientEnvironment
)
