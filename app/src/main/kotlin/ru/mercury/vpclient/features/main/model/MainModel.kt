package ru.mercury.vpclient.features.main.model

import ru.mercury.vpclient.core.mvi.Model
import androidx.navigation3.runtime.NavKey
import ru.mercury.vpclient.features.main.tabs.home.navigation.HomeRoute

data class MainModel(
    val selectedRoute: NavKey = HomeRoute
): Model
