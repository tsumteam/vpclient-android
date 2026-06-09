package ru.mercury.vpclient.features.profile_stack

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import ru.mercury.vpclient.features.profile.ProfileScreen
import ru.mercury.vpclient.features.profile.navigation.ProfileRoute
import ru.mercury.vpclient.features.profile_my_data.MyDataScreen
import ru.mercury.vpclient.features.profile_my_data.navigation.MyDataRoute
import ru.mercury.vpclient.features.profile_stack.event.ProfileStackEventManager
import ru.mercury.vpclient.shared.navigation.BackRoute
import ru.mercury.vpclient.shared.ui.components.system.ClientNavDisplay
import ru.mercury.vpclient.shared.ui.ktx.ObserveAsEvents

@Composable
fun ProfileStackScreen(
    navBackStack: NavBackStack<NavKey>
) {
    ClientNavDisplay(
        backStack = navBackStack,
        modifier = Modifier.fillMaxSize(),
        entryProvider = entryProvider {
            entry<ProfileRoute> { ProfileScreen() }
            entry<MyDataRoute> { MyDataScreen() }
        }
    )

    ObserveAsEvents(
        flow = ProfileStackEventManager.eventFlow
    ) { event ->
        when (event) {
            is MyDataRoute -> navBackStack.add(event)
            is BackRoute -> navBackStack.removeLastOrNull()
        }
    }
}
