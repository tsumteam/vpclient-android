@file:OptIn(ExperimentalAnimationApi::class)

package ru.mercury.vpclient.activity

import android.Manifest
import android.os.Build
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.activity.intent.MainActivityIntent
import ru.mercury.vpclient.features.auth_code.CodeScreen
import ru.mercury.vpclient.features.auth_code.navigation.CodeRoute
import ru.mercury.vpclient.features.auth_login.LoginScreen
import ru.mercury.vpclient.features.auth_login.navigation.LoginRoute
import ru.mercury.vpclient.features.auth_register.RegisterScreen
import ru.mercury.vpclient.features.auth_register.navigation.RegisterRoute
import ru.mercury.vpclient.features.auth_welcome.WelcomeScreen
import ru.mercury.vpclient.features.auth_welcome.navigation.WelcomeRoute
import ru.mercury.vpclient.features.cart.CartScreen
import ru.mercury.vpclient.features.cart.navigation.CartRoute
import ru.mercury.vpclient.features.compilation.CompilationScreen
import ru.mercury.vpclient.features.compilation.navigation.CompilationRoute
import ru.mercury.vpclient.features.consultant.ConsultantScreen
import ru.mercury.vpclient.features.consultant.navigation.ConsultantRoute
import ru.mercury.vpclient.features.details.DetailsScreen
import ru.mercury.vpclient.features.details.navigation.DetailsRoute
import ru.mercury.vpclient.features.fitting_addresses.FittingAddressesScreen
import ru.mercury.vpclient.features.fitting_addresses.navigation.FittingAddressesRoute
import ru.mercury.vpclient.features.fitting_confirmation.FittingConfirmationScreen
import ru.mercury.vpclient.features.fitting_confirmation.navigation.FittingConfirmationRoute
import ru.mercury.vpclient.features.fitting_info.FittingInfoScreen
import ru.mercury.vpclient.features.fitting_info.navigation.FittingInfoRoute
import ru.mercury.vpclient.features.fitting_success.FittingSuccessScreen
import ru.mercury.vpclient.features.fitting_success.navigation.FittingSuccessRoute
import ru.mercury.vpclient.features.main.MainScreen
import ru.mercury.vpclient.features.main.navigation.MainRoute
import ru.mercury.vpclient.features.media.MediaScreen
import ru.mercury.vpclient.features.media.navigation.MediaRoute
import ru.mercury.vpclient.features.profile_brands.ProfileBrandsScreen
import ru.mercury.vpclient.features.profile_brands.navigation.ProfileBrandsRoute
import ru.mercury.vpclient.features.profile_loyalty_info.ProfileLoyaltyInfoScreen
import ru.mercury.vpclient.features.profile_loyalty_info.navigation.ProfileLoyaltyInfoRoute
import ru.mercury.vpclient.features.profile_loyalty_qr.ProfileLoyaltyQrScreen
import ru.mercury.vpclient.features.profile_loyalty_qr.navigation.ProfileLoyaltyQrRoute
import ru.mercury.vpclient.features.profile_loyalty_terms.ProfileLoyaltyTermsScreen
import ru.mercury.vpclient.features.profile_loyalty_terms.navigation.ProfileLoyaltyTermsRoute
import ru.mercury.vpclient.features.profile_qr.ProfileQrScreen
import ru.mercury.vpclient.features.profile_qr.navigation.ProfileQrRoute
import ru.mercury.vpclient.features.video.VideoScreen
import ru.mercury.vpclient.features.video.navigation.VideoRoute
import ru.mercury.vpclient.shared.data.event.CenterLoading
import ru.mercury.vpclient.shared.navigation.BackRoute
import ru.mercury.vpclient.shared.ui.components.LoadingBox
import ru.mercury.vpclient.shared.ui.components.system.ClientNavDisplay
import ru.mercury.vpclient.shared.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.shared.ui.ktx.rememberRequestMultiplePermissions

@Composable
fun MainActivityContent(
    viewModel: MainActivityViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    if (state.startDestination == null) return

    val navBackStack: NavBackStack<NavKey> = rememberNavBackStack(requireNotNull(state.startDestination))
    val requestPermissions = rememberRequestMultiplePermissions(if (Build.VERSION.SDK_INT >= 33) arrayOf(Manifest.permission.POST_NOTIFICATIONS) else emptyArray())

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ClientNavDisplay(
            backStack = navBackStack,
            modifier = Modifier.fillMaxSize(),
            entryProvider = entryProvider {
                entry<WelcomeRoute> { WelcomeScreen() }
                entry<RegisterRoute> { RegisterScreen() }
                entry<LoginRoute> { LoginScreen() }
                entry<CodeRoute> { CodeScreen() }
                entry<MainRoute> { MainScreen(it) }
                entry<ProfileQrRoute> { ProfileQrScreen() }
                entry<ProfileLoyaltyInfoRoute> { ProfileLoyaltyInfoScreen() }
                entry<ProfileLoyaltyQrRoute> { ProfileLoyaltyQrScreen(it) }
                entry<ProfileLoyaltyTermsRoute> { ProfileLoyaltyTermsScreen(it) }
                entry<ProfileBrandsRoute> { ProfileBrandsScreen() }
                entry<CartRoute> { CartScreen(it) }
                entry<DetailsRoute> { DetailsScreen(it) }
                entry<CompilationRoute> { CompilationScreen(it) }
                entry<FittingConfirmationRoute> { FittingConfirmationScreen(it) }
                entry<FittingInfoRoute> { FittingInfoScreen(it) }
                entry<FittingAddressesRoute> { FittingAddressesScreen(it) }
                entry<FittingSuccessRoute> { FittingSuccessScreen(it) }
                entry<ConsultantRoute> { ConsultantScreen(it) }
                entry<MediaRoute> { MediaScreen(it) }
                entry<VideoRoute> { VideoScreen(it) }
            }
        )

        LoadingBox(
            isVisible = state.centerLoading
        )
    }

    LaunchedEffect(Unit) { requestPermissions() }

    ObserveAsEvents(
        flow = MainEventManager.eventFlow
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
                if (event is BackRoute) {
                    viewModel.dispatch(MainActivityIntent.CenterLoading(enabled = false))
                }
            }
            is CenterLoading -> viewModel.dispatch(MainActivityIntent.CenterLoading(event.enabled))
        }
    }
}
