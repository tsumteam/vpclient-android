package ru.mercury.vpclient.features.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
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
import ru.mercury.vpclient.core.ui.theme.VPClientIcons
import ru.mercury.vpclient.core.ui.theme.VPClientStrings
import ru.mercury.vpclient.core.ui.theme.VPClientTheme
import ru.mercury.vpclient.core.ui.theme.secondary2
import ru.mercury.vpclient.core.ui.theme.surface4
import ru.mercury.vpclient.features.main.event.MainTabsEventManager
import ru.mercury.vpclient.features.main.intent.MainIntent
import ru.mercury.vpclient.features.main.model.MainModel
import ru.mercury.vpclient.features.main.tabs.cash.CashScreen
import ru.mercury.vpclient.features.main.tabs.cash.navigation.CashRoute
import ru.mercury.vpclient.features.main.tabs.home.HomeScreen
import ru.mercury.vpclient.features.main.tabs.home.navigation.HomeRoute
import ru.mercury.vpclient.features.main.tabs.profile.ProfileScreen
import ru.mercury.vpclient.features.main.tabs.profile.navigation.ProfileRoute

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val mainBackStack: NavBackStack<NavKey> = rememberNavBackStack(state.selectedRoute)

    LaunchedEffect(state.selectedRoute) {
        mainBackStack.setRoot(state.selectedRoute)
    }

    MainScreenContent(
        state = state,
        dispatch = viewModel::dispatch,
        navBackStack = mainBackStack
    )

    ObserveAsEvents(
        flow = MainTabsEventManager.eventFlow
    ) { event ->
        when (event) {
            is Route -> viewModel.dispatch(MainIntent.SelectTab(event))
        }
    }
}

@Composable
private fun MainScreenContent(
    state: MainModel,
    dispatch: (MainIntent) -> Unit,
    navBackStack: NavBackStack<NavKey>
) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (navHost, bottomColumn) = createRefs()

        NavDisplay(
            backStack = navBackStack,
            onBack = { navBackStack.navigateTo(BackRoute) },
            modifier = Modifier.constrainAs(navHost) {
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                bottom.linkTo(bottomColumn.top)
            },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                entry<HomeRoute> { HomeScreen() }
                entry<ProfileRoute> { ProfileScreen() }
                entry<CashRoute> { CashScreen() }
            }
        )

        Column(
            modifier = Modifier.constrainAs(bottomColumn) {
                width = Dimension.fillToConstraints
                height = Dimension.wrapContent
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }
        ) {
            BottomAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(),
                containerColor = MaterialTheme.colorScheme.surface4
            ) {
                NavigationBarItem(
                    selected = state.selectedRoute == HomeRoute,
                    onClick = {
                        when {
                            state.selectedRoute != HomeRoute -> dispatch(MainIntent.SelectTab(HomeRoute))
                            else -> {
                                navBackStack.setRoot(state.selectedRoute)
                            }
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(VPClientIcons.Home),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(VPClientStrings.AppName)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors().copy(
                        selectedIndicatorColor = MaterialTheme.colorScheme.secondary2
                    )
                )

                NavigationBarItem(
                    selected = state.selectedRoute == ProfileRoute,
                    onClick = {
                        when {
                            state.selectedRoute != ProfileRoute -> dispatch(MainIntent.SelectTab(ProfileRoute))
                            else -> {
                                navBackStack.setRoot(state.selectedRoute)
                            }
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(VPClientIcons.Home),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(VPClientStrings.AppName)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors().copy(
                        selectedIndicatorColor = MaterialTheme.colorScheme.secondary2
                    )
                )

                NavigationBarItem(
                    selected = state.selectedRoute == CashRoute,
                    onClick = {
                        when {
                            state.selectedRoute != CashRoute -> dispatch(MainIntent.SelectTab(CashRoute))
                            else -> {
                                navBackStack.setRoot(state.selectedRoute)
                            }
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(VPClientIcons.Home),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(VPClientStrings.AppName)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors().copy(
                        selectedIndicatorColor = MaterialTheme.colorScheme.secondary2
                    )
                )
            }
        }
    }
}

@Preview
@Composable
private fun MainScreenContentPreview() {
    VPClientTheme {
        MainScreenContent(
            state = MainModel(),
            dispatch = {},
            navBackStack = rememberNavBackStack(HomeRoute)
        )
    }
}
