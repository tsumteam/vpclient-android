package ru.mercury.vpclient.features.debug.model

import ru.mercury.vpclient.shared.mvi.Model
import ru.mercury.vpclient.shared.data.network.env.ClientEnvironment

data class DebugModel(
    val deviceId: String = "",
    val userToken: String = "",
    val environment: ClientEnvironment = ClientEnvironment.TEST,
    val requestDelayEnabled: Boolean = false,
    val environmentDialog: Boolean = false
): Model
