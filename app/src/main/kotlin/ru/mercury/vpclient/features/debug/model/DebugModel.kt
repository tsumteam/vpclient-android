package ru.mercury.vpclient.features.debug.model

import ru.mercury.vpclient.core.network.env.VPClientEnvironment
import ru.mercury.vpclient.core.mvi.Model

data class DebugModel(
    val deviceId: String = "",
    val userToken: String = "",
    val autofill: Boolean = false,
    val requestDelayMs: Float = 0F,
    val environment: VPClientEnvironment = VPClientEnvironment.BCA,
    val environmentDialog: Boolean = false
): Model
