@file:OptIn(ExperimentalAnimationApi::class)

package ru.mercury.vpclient.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import ru.mercury.vpclient.core.event.CenterLoading
import ru.mercury.vpclient.core.navigation.BackRoute
import ru.mercury.vpclient.core.ui.components.LoadingBox
import ru.mercury.vpclient.core.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.core.ui.ktx.rememberRequestMultiplePermissions
import ru.mercury.vpclient.features.code.CodeScreen
import ru.mercury.vpclient.features.code.navigation.CodeRoute
import ru.mercury.vpclient.features.consultant.ConsultantScreen
import ru.mercury.vpclient.features.consultant.navigation.ConsultantRoute
import ru.mercury.vpclient.features.login.LoginScreen
import ru.mercury.vpclient.features.login.navigation.LoginRoute
import ru.mercury.vpclient.features.main.MainScreen
import ru.mercury.vpclient.features.main.navigation.MainRoute
import ru.mercury.vpclient.features.register.RegisterScreen
import ru.mercury.vpclient.features.register.navigation.RegisterRoute
import ru.mercury.vpclient.features.welcome.WelcomeScreen
import ru.mercury.vpclient.features.welcome.navigation.WelcomeRoute
import ru.mercury.vpclient.main.event.MainEventManager
import ru.mercury.vpclient.main.intent.MainActivityIntent

@Composable
fun MainActivityContent(
    viewModel: MainActivityViewModel = hiltViewModel(),
    requestPermissions: () -> Unit = rememberRequestMultiplePermissions()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    if (state.startDestination == null) return

    val navBackStack: NavBackStack<NavKey> = rememberNavBackStack(requireNotNull(state.startDestination))

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        NavDisplay(
            backStack = navBackStack,
            onBack = { if (navBackStack.size > 1) navBackStack.removeLastOrNull() },
            modifier = Modifier.fillMaxSize(),
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            popTransitionSpec = { fadeIn() togetherWith fadeOut() using SizeTransform(clip = false) },
            predictivePopTransitionSpec = { fadeIn() togetherWith fadeOut() using SizeTransform(clip = false) },
            entryProvider = entryProvider {
                entry<WelcomeRoute> { WelcomeScreen() }
                entry<RegisterRoute> { RegisterScreen() }
                entry<LoginRoute> { LoginScreen() }
                entry<CodeRoute> { CodeScreen() }
                entry<MainRoute> { MainScreen() }
                entry<ConsultantRoute> { ConsultantScreen(consultantId = it.consultantId) }
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
