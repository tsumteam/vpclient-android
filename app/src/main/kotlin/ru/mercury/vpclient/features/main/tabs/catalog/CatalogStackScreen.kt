package ru.mercury.vpclient.features.main.tabs.catalog

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import ru.mercury.vpclient.shared.navigation.BackRoute
import ru.mercury.vpclient.shared.ui.components.system.ClientNavDisplay
import ru.mercury.vpclient.shared.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.features.main.tabs.catalog.event.CatalogStackEventManager
import ru.mercury.vpclient.features.main.tabs.catalog.stack.category.CategoryScreen
import ru.mercury.vpclient.features.main.tabs.catalog.stack.category.navigation.CategoryRoute
import ru.mercury.vpclient.features.main.tabs.catalog.stack.catalog.CatalogScreen
import ru.mercury.vpclient.features.main.tabs.catalog.stack.catalog.navigation.CatalogRoute
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter.FilterScreen
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter.navigation.FilterRoute

@Composable
fun CatalogStackScreen(
    navBackStack: NavBackStack<NavKey>
) {
    ClientNavDisplay(
        backStack = navBackStack,
        modifier = Modifier.fillMaxSize(),
        entryProvider = entryProvider {
            entry<CatalogRoute> { CatalogScreen() }
            entry<CategoryRoute> { CategoryScreen(it) }
            entry<FilterRoute> { FilterScreen(it) }
        }
    )

    ObserveAsEvents(
        flow = CatalogStackEventManager.eventFlow
    ) { event ->
        when (event) {
            is CategoryRoute -> navBackStack.add(event)
            is FilterRoute -> navBackStack.add(event)
            is BackRoute -> navBackStack.removeLastOrNull()
        }
    }
}
