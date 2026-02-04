package ru.mercury.vpclient.features.debug.model

import ru.mercury.vpclient.core.mvi.Model
import ru.mercury.vpclient.core.network.env.VPClientEnvironment

data class DebugModel(
    val deviceId: String = "",
    val userToken: String = "",
    val environment: VPClientEnvironment = VPClientEnvironment.TEST,
    val environmentDialog: Boolean = false
): Model
