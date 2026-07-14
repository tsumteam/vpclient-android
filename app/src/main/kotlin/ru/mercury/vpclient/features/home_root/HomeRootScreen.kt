package ru.mercury.vpclient.features.home_root

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import ru.mercury.vpclient.features.banner.BannerScreen
import ru.mercury.vpclient.features.banner.navigation.BannerRoute
import ru.mercury.vpclient.features.details.DetailsScreen
import ru.mercury.vpclient.features.details.navigation.DetailsRoute
import ru.mercury.vpclient.features.filter.FilterScreen
import ru.mercury.vpclient.features.filter.navigation.FilterRoute
import ru.mercury.vpclient.features.home.HomeScreen
import ru.mercury.vpclient.features.home.navigation.HomeRoute
import ru.mercury.vpclient.features.home_root.event.HomeRootEventManager
import ru.mercury.vpclient.shared.navigation.BackRoute
import ru.mercury.vpclient.shared.ui.components.system.ClientNavDisplay
import ru.mercury.vpclient.shared.ui.ktx.ObserveAsEvents

@Composable
fun HomeRootScreen(
    navBackStack: NavBackStack<NavKey>
) {
    ClientNavDisplay(
        backStack = navBackStack,
        modifier = Modifier.fillMaxSize(),
        entryProvider = entryProvider {
            entry<HomeRoute> { HomeScreen() }
            entry<BannerRoute> { BannerScreen(it) }
            entry<FilterRoute> { FilterScreen(it) }
            entry<DetailsRoute> { DetailsScreen(it) }
        }
    )

    ObserveAsEvents(
        flow = HomeRootEventManager.eventFlow
    ) { event ->
        when (event) {
            is BannerRoute -> navBackStack.add(event)
            is FilterRoute -> navBackStack.add(event)
            is DetailsRoute -> navBackStack.add(event)
            is BackRoute -> navBackStack.removeLastOrNull()
        }
    }
}
