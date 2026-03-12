package ru.mercury.vpclient.features.main.tabs.catalog

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import ru.mercury.vpclient.core.navigation.BackRoute
import ru.mercury.vpclient.core.ui.components.CourierNavDisplay
import ru.mercury.vpclient.core.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.features.main.tabs.catalog.event.CatalogStackEventManager
import ru.mercury.vpclient.features.main.tabs.catalog.stack.catalog.CatalogScreen
import ru.mercury.vpclient.features.main.tabs.catalog.stack.catalog.navigation.CatalogRoute
import ru.mercury.vpclient.features.main.tabs.catalog.stack.subcategory.SubcategoryScreen
import ru.mercury.vpclient.features.main.tabs.catalog.stack.subcategory.navigation.SubcategoryRoute

@Composable
fun CatalogStackScreen(
    navBackStack: NavBackStack<NavKey>
) {
    CourierNavDisplay(
        backStack = navBackStack,
        modifier = Modifier.fillMaxSize(),
        entryProvider = entryProvider {
            entry<CatalogRoute> { CatalogScreen() }
            entry<SubcategoryRoute> { SubcategoryScreen(it) }
        }
    )

    ObserveAsEvents(
        flow = CatalogStackEventManager.eventFlow
    ) { event ->
        when (event) {
            is SubcategoryRoute -> navBackStack.add(event)
            is BackRoute -> navBackStack.removeLastOrNull()
        }
    }
}
