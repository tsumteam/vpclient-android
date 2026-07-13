package ru.mercury.vpclient.features.brand_root

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import ru.mercury.vpclient.features.brand_root.event.BrandRootEventManager
import ru.mercury.vpclient.features.brands.BrandsScreen
import ru.mercury.vpclient.features.brands.navigation.BrandsRoute
import ru.mercury.vpclient.features.details.DetailsScreen
import ru.mercury.vpclient.features.details.navigation.DetailsRoute
import ru.mercury.vpclient.features.filter.FilterScreen
import ru.mercury.vpclient.features.filter.navigation.FilterRoute
import ru.mercury.vpclient.shared.navigation.BackRoute
import ru.mercury.vpclient.shared.ui.components.system.ClientNavDisplay
import ru.mercury.vpclient.shared.ui.ktx.ObserveAsEvents

@Composable
fun BrandRootScreen(
    navBackStack: NavBackStack<NavKey>
) {
    ClientNavDisplay(
        backStack = navBackStack,
        modifier = Modifier.fillMaxSize(),
        entryProvider = entryProvider {
            entry<BrandsRoute> { BrandsScreen() }
            entry<FilterRoute> { FilterScreen(it) }
            entry<DetailsRoute> { DetailsScreen(it) }
        }
    )

    ObserveAsEvents(
        flow = BrandRootEventManager.eventFlow
    ) { event ->
        when (event) {
            is FilterRoute -> navBackStack.add(event)
            is DetailsRoute -> navBackStack.add(event)
            is BackRoute -> navBackStack.removeLastOrNull()
        }
    }
}
