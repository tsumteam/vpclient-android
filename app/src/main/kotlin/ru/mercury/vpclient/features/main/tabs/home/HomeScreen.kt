package ru.mercury.vpclient.features.main.tabs.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import ru.mercury.vpclient.core.navigation.BackRoute
import ru.mercury.vpclient.core.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.features.main.tabs.home.event.HomeEventManager
import ru.mercury.vpclient.features.main.tabs.home.stack.routes.RoutesScreen
import ru.mercury.vpclient.features.main.tabs.home.stack.routes.navigation.RoutesRoute
import ru.mercury.vpclient.features.main.navigation.MainRoute
import ru.mercury.vpclient.features.register.navigation.RegisterRoute
import ru.mercury.vpclient.features.welcome.navigation.WelcomeRoute

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val navBackStack: NavBackStack<NavKey> = rememberNavBackStack(state.selectedRoute)

    LaunchedEffect(state.selectedRoute) {
        navBackStack.clear()
        navBackStack.add(state.selectedRoute)
    }

    NavDisplay(
        backStack = navBackStack,
        onBack = {
            if (navBackStack.size > 1) {
                navBackStack.removeLastOrNull()
            }
        },
        modifier = Modifier.fillMaxSize(),
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<RoutesRoute> { RoutesScreen() }
        }
    )

    ObserveAsEvents(
        flow = HomeEventManager.eventFlow
    ) { event ->
        when (event) {
            is NavKey -> {
                when (event) {
                    is BackRoute -> {
                        if (navBackStack.size > 1) {
                            navBackStack.removeLastOrNull()
                        }
                    }
                    is WelcomeRoute -> {
                        navBackStack.clear()
                        navBackStack.add(event)
                    }
                    is RegisterRoute -> {
                        val mainIndex = navBackStack.indexOfLast { it is MainRoute }
                        if (mainIndex != -1) {
                            while (navBackStack.lastIndex >= mainIndex) {
                                navBackStack.removeAt(navBackStack.lastIndex)
                            }
                        }
                        if (navBackStack.lastOrNull() != event) {
                            navBackStack.add(event)
                        }
                    }
                    is MainRoute -> {
                        if (event.popUpToMain) {
                            val mainIndex = navBackStack.indexOfLast { it is MainRoute }
                            if (mainIndex != -1) {
                                while (navBackStack.lastIndex >= mainIndex) {
                                    navBackStack.removeAt(navBackStack.lastIndex)
                                }
                            }
                        } else {
                            val welcomeIndex = navBackStack.indexOfLast { it is WelcomeRoute }
                            if (welcomeIndex != -1) {
                                while (navBackStack.lastIndex >= welcomeIndex) {
                                    navBackStack.removeAt(navBackStack.lastIndex)
                                }
                            }
                        }
                        if (navBackStack.lastOrNull() != event) {
                            navBackStack.add(event)
                        }
                    }
                    else -> navBackStack.add(event)
                }
            }
        }
    }
}
