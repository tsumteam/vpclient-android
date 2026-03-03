package ru.mercury.vpclient.main.model

import ru.mercury.vpclient.core.mvi.Model
import androidx.navigation3.runtime.NavKey

data class MainActivityModel(
    val splashLoading: Boolean = true,
    val startDestination: NavKey? = null,
    val centerLoading: Boolean = false,
    val isConnected: Boolean = true,
    val isReconnectVisible: Boolean = false
): Model
