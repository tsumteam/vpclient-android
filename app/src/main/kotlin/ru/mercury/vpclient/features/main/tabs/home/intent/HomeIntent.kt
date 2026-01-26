package ru.mercury.vpclient.features.main.tabs.home.intent

import ru.mercury.vpclient.core.mvi.Intent
import ru.mercury.vpclient.core.navigation.Route

sealed interface HomeIntent: Intent {
    data class NavigateRoute(val route: Route): HomeIntent
}
