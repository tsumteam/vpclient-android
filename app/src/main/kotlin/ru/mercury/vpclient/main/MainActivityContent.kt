@file:OptIn(ExperimentalAnimationApi::class)

package ru.mercury.vpclient.main

import android.content.ClipData
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import kotlinx.coroutines.launch
import ru.mercury.vpclient.core.event.CenterLoading
import ru.mercury.vpclient.core.event.ClipboardEvent
import ru.mercury.vpclient.core.event.SnackbarBottomBarErrorEvent
import ru.mercury.vpclient.core.event.SnackbarBottomBarEvent
import ru.mercury.vpclient.core.event.SnackbarBottomBarFabErrorEvent
import ru.mercury.vpclient.core.event.SnackbarBottomBarFabEvent
import ru.mercury.vpclient.core.event.SnackbarErrorEvent
import ru.mercury.vpclient.core.event.SnackbarEvent
import ru.mercury.vpclient.core.event.SnackbarFabErrorEvent
import ru.mercury.vpclient.core.event.SnackbarFabEvent
import ru.mercury.vpclient.core.event.SnackbarTopErrorEvent
import ru.mercury.vpclient.core.ktx.snackbarMessage
import ru.mercury.vpclient.core.navigation.BackRoute
import ru.mercury.vpclient.core.navigation.Route
import ru.mercury.vpclient.core.navigation.navigateTo
import ru.mercury.vpclient.core.ui.components.LoadingBox
import ru.mercury.vpclient.core.ui.components.VPClientSnackbarHost
import ru.mercury.vpclient.core.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.core.ui.ktx.rememberRequestMultiplePermissions
import ru.mercury.vpclient.features.authentication.AuthenticationScreen
import ru.mercury.vpclient.features.authentication.navigation.AuthenticationRoute
import ru.mercury.vpclient.features.main.MainScreen
import ru.mercury.vpclient.features.main.navigation.MainRoute
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

    val context = LocalContext.current
    val clipboard = LocalClipboard.current
    val hapticFeedback = LocalHapticFeedback.current
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarHostStateError = remember { SnackbarHostState() }
    val snackbarHostStateFab = remember { SnackbarHostState() }
    val snackbarHostStateFabError = remember { SnackbarHostState() }
    val snackbarHostStateBottomBar = remember { SnackbarHostState() }
    val snackbarHostStateBottomBarError = remember { SnackbarHostState() }
    val snackbarHostStateBottomBarFab = remember { SnackbarHostState() }
    val snackbarHostStateBottomBarFabError = remember { SnackbarHostState() }
    val snackbarHostStateTopError = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val connectionStateTargetPadding = 0.dp
        val snackbarPadding = if (state.isReconnectVisible) 60.dp else 0.dp
        val bottomPadding by animateDpAsState(targetValue = 0.dp, animationSpec = tween(easing = LinearOutSlowInEasing), label = "bottomPadding")

        NavDisplay(
            backStack = navBackStack,
            onBack = { navBackStack.navigateTo(BackRoute) },
            modifier = Modifier
                .padding(bottom = bottomPadding)
                .fillMaxSize(),
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                entry<AuthenticationRoute> { AuthenticationScreen() }
                entry<MainRoute> { MainScreen() }
            }
        )

        VPClientSnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .navigationBarsPadding()
                .padding(bottom = connectionStateTargetPadding)
                .align(Alignment.BottomCenter)
        )

        VPClientSnackbarHost(
            hostState = snackbarHostStateError,
            modifier = Modifier
                .navigationBarsPadding()
                .padding(bottom = connectionStateTargetPadding)
                .align(Alignment.BottomCenter),
            containerColor = MaterialTheme.colorScheme.error
        )

        VPClientSnackbarHost(
            hostState = snackbarHostStateFab,
            modifier = Modifier
                .navigationBarsPadding()
                .padding(bottom = 72.dp + connectionStateTargetPadding)
                .align(Alignment.BottomCenter)
        )

        VPClientSnackbarHost(
            hostState = snackbarHostStateFabError,
            modifier = Modifier
                .navigationBarsPadding()
                .padding(bottom = 72.dp + connectionStateTargetPadding)
                .align(Alignment.BottomCenter),
            containerColor = MaterialTheme.colorScheme.error
        )

        VPClientSnackbarHost(
            hostState = snackbarHostStateBottomBar,
            modifier = Modifier
                .navigationBarsPadding()
                .padding(bottom = 80.dp + snackbarPadding)
                .align(Alignment.BottomCenter)
        )

        VPClientSnackbarHost(
            hostState = snackbarHostStateBottomBarError,
            modifier = Modifier
                .navigationBarsPadding()
                .padding(bottom = 80.dp + snackbarPadding)
                .align(Alignment.BottomCenter),
            containerColor = MaterialTheme.colorScheme.error
        )

        VPClientSnackbarHost(
            hostState = snackbarHostStateBottomBarFab,
            modifier = Modifier
                .navigationBarsPadding()
                .padding(bottom = 152.dp + snackbarPadding)
                .align(Alignment.BottomCenter)
        )

        VPClientSnackbarHost(
            hostState = snackbarHostStateBottomBarFabError,
            modifier = Modifier
                .navigationBarsPadding()
                .padding(bottom = 152.dp + snackbarPadding)
                .align(Alignment.BottomCenter),
            containerColor = MaterialTheme.colorScheme.error
        )

        VPClientSnackbarHost(
            hostState = snackbarHostStateTopError,
            modifier = Modifier
                .statusBarsPadding()
                .align(Alignment.TopCenter),
            containerColor = MaterialTheme.colorScheme.error
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
            is Route -> {
                navBackStack.navigateTo(event)
                if (event is BackRoute) {
                    viewModel.dispatch(MainActivityIntent.CenterLoading(enabled = false))
                }
            }
            is ClipboardEvent -> scope.launch { clipboard.setClipEntry(ClipEntry(ClipData.newPlainText("", event.content))) }
            is CenterLoading -> viewModel.dispatch(MainActivityIntent.CenterLoading(event.enabled))
            is SnackbarEvent -> {
                snackbarHostState.run {
                    currentSnackbarData?.dismiss()
                    scope.launch { showSnackbar(event.snackbarMessage(context)) }
                }
            }
            is SnackbarErrorEvent -> {
                snackbarHostStateError.run {
                    currentSnackbarData?.dismiss()
                    scope.launch { showSnackbar(event.snackbarMessage(context)) }
                }
            }
            is SnackbarFabEvent -> {
                snackbarHostStateFab.run {
                    currentSnackbarData?.dismiss()
                    scope.launch { showSnackbar(event.snackbarMessage(context)) }
                }
            }
            is SnackbarFabErrorEvent -> {
                snackbarHostStateFabError.run {
                    currentSnackbarData?.dismiss()
                    scope.launch { showSnackbar(event.snackbarMessage(context)) }
                }
            }
            is SnackbarBottomBarEvent -> {
                snackbarHostStateBottomBar.run {
                    currentSnackbarData?.dismiss()
                    scope.launch { showSnackbar(event.snackbarMessage(context)) }
                }
            }
            is SnackbarBottomBarErrorEvent -> {
                snackbarHostStateBottomBarError.run {
                    currentSnackbarData?.dismiss()
                    scope.launch { showSnackbar(event.snackbarMessage(context)) }
                }
            }
            is SnackbarBottomBarFabEvent -> {
                snackbarHostStateBottomBarFab.run {
                    currentSnackbarData?.dismiss()
                    scope.launch { showSnackbar(event.snackbarMessage(context)) }
                }
            }
            is SnackbarBottomBarFabErrorEvent -> {
                snackbarHostStateBottomBarFabError.run {
                    currentSnackbarData?.dismiss()
                    scope.launch { showSnackbar(event.snackbarMessage(context)) }
                }
            }
            is SnackbarTopErrorEvent -> {
                snackbarHostStateTopError.run {
                    currentSnackbarData?.dismiss()
                    scope.launch { showSnackbar(event.snackbarMessage(context)) }
                }
            }
        }
    }
}
