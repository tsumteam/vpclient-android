package ru.mercury.vpclient.core.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import ru.mercury.vpclient.features.authentication.navigation.AuthenticationRoute
import ru.mercury.vpclient.features.main.navigation.MainRoute

fun NavBackStack<NavKey>.navigateTo(route: Route) {
    when (route) {
        is BackRoute -> popBackStack()
        is AuthenticationRoute -> {
            popUpTo<MainRoute>(inclusive = true)
            push(route, singleTop = true)
        }
        is MainRoute -> {
            when {
                route.popUpToMain -> {
                    popUpTo<MainRoute>(inclusive = true)
                    push(route, singleTop = true)
                }
                else -> {
                    popUpTo<AuthenticationRoute>(inclusive = true)
                    push(route, singleTop = true)
                }
            }
        }
        else -> push(route)
    }
}

fun NavBackStack<NavKey>.setRoot(route: Route) {
    clear()
    add(route)
}

private fun NavBackStack<NavKey>.push(route: Route, singleTop: Boolean = false) {
    if (singleTop && lastOrNull() == route) return
    add(route)
}

private fun NavBackStack<NavKey>.popBackStack() {
    if (size > 1) removeAt(lastIndex)
}

private inline fun <reified T : Route> NavBackStack<NavKey>.popUpTo(inclusive: Boolean) {
    val index = indexOfLast { it is T }
    if (index == -1) return
    val removeFrom = if (inclusive) index else index + 1
    while (lastIndex >= removeFrom) removeAt(lastIndex)
}
