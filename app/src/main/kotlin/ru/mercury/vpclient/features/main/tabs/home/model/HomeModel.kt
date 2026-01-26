package ru.mercury.vpclient.features.main.tabs.home.model

import ru.mercury.vpclient.core.mvi.Model
import ru.mercury.vpclient.core.navigation.Route
import ru.mercury.vpclient.features.main.tabs.home.stack.routes.navigation.RoutesRoute

data class HomeModel(
    val selectedRoute: Route = RoutesRoute
): Model
