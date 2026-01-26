package ru.mercury.vpclient.features.main.model

import ru.mercury.vpclient.core.mvi.Model
import ru.mercury.vpclient.core.navigation.Route
import ru.mercury.vpclient.features.main.tabs.home.navigation.HomeRoute

data class MainModel(
    val selectedRoute: Route = HomeRoute
): Model
