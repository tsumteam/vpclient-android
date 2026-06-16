package ru.mercury.vpclient.features.profile_stack

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import ru.mercury.vpclient.features.profile.ProfileScreen
import ru.mercury.vpclient.features.profile.navigation.ProfileRoute
import ru.mercury.vpclient.features.profile_order.ProfileOrderScreen
import ru.mercury.vpclient.features.profile_order.navigation.ProfileOrderRoute
import ru.mercury.vpclient.features.profile_contacts.ProfileContactsScreen
import ru.mercury.vpclient.features.profile_contacts.navigation.ProfileContactsRoute
import ru.mercury.vpclient.features.profile_delivery.ProfileDeliveryScreen
import ru.mercury.vpclient.features.profile_delivery.navigation.ProfileDeliveryRoute
import ru.mercury.vpclient.features.profile_gift.ProfileGiftScreen
import ru.mercury.vpclient.features.profile_gift.navigation.ProfileGiftRoute
import ru.mercury.vpclient.features.profile_info.ProfileInfoScreen
import ru.mercury.vpclient.features.profile_info.navigation.ProfileInfoRoute
import ru.mercury.vpclient.features.profile_my_data.MyDataScreen
import ru.mercury.vpclient.features.profile_my_data.navigation.MyDataRoute
import ru.mercury.vpclient.features.profile_orders.ProfileOrdersScreen
import ru.mercury.vpclient.features.profile_orders.navigation.ProfileOrdersRoute
import ru.mercury.vpclient.features.profile_payment.ProfilePaymentScreen
import ru.mercury.vpclient.features.profile_payment.navigation.ProfilePaymentRoute
import ru.mercury.vpclient.features.profile_policy.ProfilePolicyScreen
import ru.mercury.vpclient.features.profile_policy.navigation.ProfilePolicyRoute
import ru.mercury.vpclient.features.profile_return.ProfileReturnScreen
import ru.mercury.vpclient.features.profile_return.navigation.ProfileReturnRoute
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
            entry<ProfileOrdersRoute> { ProfileOrdersScreen() }
            entry<ProfileOrderRoute> { ProfileOrderScreen(it) }
            entry<ProfileInfoRoute> { ProfileInfoScreen() }
            entry<ProfilePaymentRoute> { ProfilePaymentScreen() }
            entry<ProfileDeliveryRoute> { ProfileDeliveryScreen() }
            entry<ProfileReturnRoute> { ProfileReturnScreen() }
            entry<ProfilePolicyRoute> { ProfilePolicyScreen() }
            entry<ProfileGiftRoute> { ProfileGiftScreen() }
            entry<ProfileContactsRoute> { ProfileContactsScreen() }
        }
    )

    ObserveAsEvents(
        flow = ProfileStackEventManager.eventFlow
    ) { event ->
        when (event) {
            is ProfileRoute -> {
                navBackStack.clear()
                navBackStack.add(ProfileRoute)
            }
            is MyDataRoute -> navBackStack.add(event)
            is ProfileOrdersRoute -> navBackStack.add(event)
            is ProfileOrderRoute -> navBackStack.add(event)
            is ProfileInfoRoute -> navBackStack.add(event)
            is ProfilePaymentRoute -> navBackStack.add(event)
            is ProfileDeliveryRoute -> navBackStack.add(event)
            is ProfileReturnRoute -> navBackStack.add(event)
            is ProfilePolicyRoute -> navBackStack.add(event)
            is ProfileGiftRoute -> navBackStack.add(event)
            is ProfileContactsRoute -> navBackStack.add(event)
            is BackRoute -> navBackStack.removeLastOrNull()
        }
    }
}
