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
import ru.mercury.vpclient.core.navigation.Route
import ru.mercury.vpclient.core.navigation.navigateTo
import ru.mercury.vpclient.core.navigation.setRoot
import ru.mercury.vpclient.core.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.features.main.tabs.home.event.HomeEventManager
import ru.mercury.vpclient.features.main.tabs.home.stack.routes.RoutesScreen
import ru.mercury.vpclient.features.main.tabs.home.stack.routes.navigation.RoutesRoute

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val navBackStack: NavBackStack<NavKey> = rememberNavBackStack(state.selectedRoute)

    LaunchedEffect(state.selectedRoute) {
        navBackStack.setRoot(state.selectedRoute)
    }

    NavDisplay(
        backStack = navBackStack,
        onBack = { navBackStack.navigateTo(BackRoute) },
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
            is Route -> navBackStack.navigateTo(event)
        }
    }
}
