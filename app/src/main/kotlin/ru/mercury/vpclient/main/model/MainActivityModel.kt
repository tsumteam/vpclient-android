package ru.mercury.vpclient.main.model

import ru.mercury.vpclient.core.mvi.Model
import ru.mercury.vpclient.core.navigation.Route

data class MainActivityModel(
    val splashLoading: Boolean = true,
    val startDestination: Route? = null,
    val centerLoading: Boolean = false,
    val isConnected: Boolean = true,
    val isReconnectVisible: Boolean = false
): Model
