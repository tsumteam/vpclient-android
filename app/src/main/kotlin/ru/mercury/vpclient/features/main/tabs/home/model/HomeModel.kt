package ru.mercury.vpclient.features.main.tabs.home.model

import ru.mercury.vpclient.core.mvi.Model
import androidx.navigation3.runtime.NavKey
import ru.mercury.vpclient.features.main.tabs.home.stack.routes.navigation.RoutesRoute

data class HomeModel(
    val selectedRoute: NavKey = RoutesRoute
): Model
