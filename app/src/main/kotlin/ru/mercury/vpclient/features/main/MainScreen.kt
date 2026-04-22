package ru.mercury.vpclient.features.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import ru.mercury.vpclient.R
import ru.mercury.vpclient.features.details.navigation.DetailsRoute
import ru.mercury.vpclient.features.main.event.MainTabsEventManager
import ru.mercury.vpclient.features.main.intent.MainIntent
import ru.mercury.vpclient.features.main.model.MainModel
import ru.mercury.vpclient.features.main.tabs.brands.BrandsScreen
import ru.mercury.vpclient.features.main.tabs.brands.navigation.BrandsRoute
import ru.mercury.vpclient.features.main.tabs.catalog.CatalogStackScreen
import ru.mercury.vpclient.features.main.tabs.catalog.navigation.CatalogStackRoute
import ru.mercury.vpclient.features.main.tabs.catalog.stack.catalog.navigation.CatalogRoute
import ru.mercury.vpclient.features.main.tabs.consultants.ConsultantsScreen
import ru.mercury.vpclient.features.main.tabs.consultants.navigation.ConsultantsRoute
import ru.mercury.vpclient.features.main.tabs.fitting.FittingScreen
import ru.mercury.vpclient.features.main.tabs.fitting.navigation.FittingRoute
import ru.mercury.vpclient.features.main.tabs.home.HomeScreen
import ru.mercury.vpclient.features.main.tabs.home.navigation.HomeRoute
import ru.mercury.vpclient.features.main.tabs.profile.ProfileScreen
import ru.mercury.vpclient.features.main.tabs.profile.navigation.ProfileRoute
import ru.mercury.vpclient.shared.ui.components.system.ClientNavDisplay
import ru.mercury.vpclient.shared.ui.icons.Brands24
import ru.mercury.vpclient.shared.ui.icons.Catalog24
import ru.mercury.vpclient.shared.ui.icons.Fitting24
import ru.mercury.vpclient.shared.ui.icons.Home24
import ru.mercury.vpclient.shared.ui.icons.Profile24
import ru.mercury.vpclient.shared.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.regular11
import ru.mercury.vpclient.shared.ui.theme.secondary4
import ru.mercury.vpclient.shared.ui.theme.secondary5

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val mainBackStack: NavBackStack<NavKey> = rememberNavBackStack(state.selectedRoute)
    val catalogBackStack: NavBackStack<NavKey> = rememberNavBackStack(CatalogRoute)

    LaunchedEffect(state.selectedRoute) {
        mainBackStack.clear()
        mainBackStack.add(state.selectedRoute)
    }

    MainScreenContent(
        state = state,
        dispatch = viewModel::dispatch,
        navBackStack = mainBackStack,
        catalogNavBackStack = catalogBackStack
    )

    ObserveAsEvents(
        flow = MainTabsEventManager.eventFlow
    ) { event ->
        when (event) {
            is NavKey -> viewModel.dispatch(MainIntent.SelectTab(event))
        }
    }
}

@Composable
private fun MainScreenContent(
    state: MainModel,
    dispatch: (MainIntent) -> Unit,
    navBackStack: NavBackStack<NavKey>,
    catalogNavBackStack: NavBackStack<NavKey>
) {
    val dividerColor = MaterialTheme.colorScheme.secondary5
    val navigationSuiteScaffoldState = rememberNavigationSuiteScaffoldState()
    val isBottomBarVisible by remember(state.selectedRoute, catalogNavBackStack) {
        derivedStateOf {
            when {
                state.selectedRoute != CatalogStackRoute -> true
                else -> catalogNavBackStack.lastOrNull() !is DetailsRoute
            }
        }
    }

    LaunchedEffect(isBottomBarVisible) {
        when {
            isBottomBarVisible -> navigationSuiteScaffoldState.show()
            else -> navigationSuiteScaffoldState.hide()
        }
    }

    NavigationSuiteScaffold(
        modifier = Modifier.fillMaxSize(),
        navigationItems = {
            BottomAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .drawWithContent {
                        drawContent()
                        drawRect(
                            color = dividerColor,
                            size = Size(size.width, 1.dp.toPx()),
                            topLeft = Offset.Zero
                        )
                    },
                containerColor = Color.White
            ) {
                NavigationBarItem(
                    selected = state.selectedRoute == HomeRoute,
                    onClick = {
                        when {
                            state.selectedRoute != HomeRoute -> dispatch(MainIntent.SelectTab(HomeRoute))
                            else -> {
                                navBackStack.clear()
                                navBackStack.add(state.selectedRoute)
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Home24,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(ClientStrings.MainTabHome),
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.regular11.copy(
                                textAlign = TextAlign.Center
                            )
                        )
                    },
                    colors = NavigationBarItemDefaults.colors().copy(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.secondary4,
                        unselectedTextColor = MaterialTheme.colorScheme.secondary4,
                        selectedIndicatorColor = Color.Transparent
                    )
                )

                NavigationBarItem(
                    selected = state.selectedRoute == BrandsRoute,
                    onClick = {
                        when {
                            state.selectedRoute != BrandsRoute -> dispatch(MainIntent.SelectTab(BrandsRoute))
                            else -> {
                                navBackStack.clear()
                                navBackStack.add(state.selectedRoute)
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Brands24,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(ClientStrings.MainTabBrands),
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.regular11.copy(
                                textAlign = TextAlign.Center
                            )
                        )
                    },
                    colors = NavigationBarItemDefaults.colors().copy(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.secondary4,
                        unselectedTextColor = MaterialTheme.colorScheme.secondary4,
                        selectedIndicatorColor = Color.Transparent
                    )
                )

                NavigationBarItem(
                    selected = state.selectedRoute == CatalogStackRoute,
                    onClick = {
                        when {
                            state.selectedRoute != CatalogStackRoute -> dispatch(MainIntent.SelectTab(CatalogStackRoute))
                            else -> {
                                navBackStack.clear()
                                navBackStack.add(state.selectedRoute)
                                catalogNavBackStack.clear()
                                catalogNavBackStack.add(CatalogRoute)
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Catalog24,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(ClientStrings.MainTabCatalog),
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.regular11.copy(
                                textAlign = TextAlign.Center
                            )
                        )
                    },
                    colors = NavigationBarItemDefaults.colors().copy(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.secondary4,
                        unselectedTextColor = MaterialTheme.colorScheme.secondary4,
                        selectedIndicatorColor = Color.Transparent
                    )
                )

                NavigationBarItem(
                    selected = state.selectedRoute == FittingRoute,
                    onClick = {
                        when {
                            state.selectedRoute != FittingRoute -> dispatch(MainIntent.SelectTab(FittingRoute))
                            else -> {
                                navBackStack.clear()
                                navBackStack.add(state.selectedRoute)
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Fitting24,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(ClientStrings.MainTabFitting),
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.regular11.copy(
                                textAlign = TextAlign.Center
                            )
                        )
                    },
                    colors = NavigationBarItemDefaults.colors().copy(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.secondary4,
                        unselectedTextColor = MaterialTheme.colorScheme.secondary4,
                        selectedIndicatorColor = Color.Transparent
                    )
                )

                NavigationBarItem(
                    selected = state.selectedRoute == ConsultantsRoute,
                    onClick = {
                        when {
                            state.selectedRoute != ConsultantsRoute -> dispatch(MainIntent.SelectTab(ConsultantsRoute))
                            else -> {
                                navBackStack.clear()
                                navBackStack.add(state.selectedRoute)
                            }
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(if (state.selectedRoute == ConsultantsRoute) R.drawable.ic_consultants_active_24 else R.drawable.ic_consultants_inactive_24),
                            contentDescription = null,
                            tint = Color.Unspecified
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(ClientStrings.MainTabConsultants),
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.regular11.copy(
                                textAlign = TextAlign.Center
                            )
                        )
                    },
                    colors = NavigationBarItemDefaults.colors().copy(
                        selectedIconColor = Color.Unspecified,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = Color.Unspecified,
                        unselectedTextColor = MaterialTheme.colorScheme.secondary4,
                        selectedIndicatorColor = Color.Transparent
                    )
                )

                NavigationBarItem(
                    selected = state.selectedRoute == ProfileRoute,
                    onClick = {
                        when {
                            state.selectedRoute != ProfileRoute -> dispatch(MainIntent.SelectTab(ProfileRoute))
                            else -> {
                                navBackStack.clear()
                                navBackStack.add(state.selectedRoute)
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Profile24,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(ClientStrings.MainTabProfile),
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.regular11.copy(
                                textAlign = TextAlign.Center
                            )
                        )
                    },
                    colors = NavigationBarItemDefaults.colors().copy(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.secondary4,
                        unselectedTextColor = MaterialTheme.colorScheme.secondary4,
                        selectedIndicatorColor = Color.Transparent
                    )
                )
            }
        },
        navigationSuiteType = NavigationSuiteType.ShortNavigationBarCompact,
        navigationSuiteColors = NavigationSuiteDefaults.colors(
            shortNavigationBarContainerColor = Color.White
        ),
        state = navigationSuiteScaffoldState
    ) {
        ClientNavDisplay(
            backStack = navBackStack,
            onBack = {
                if (navBackStack.size > 1) {
                    navBackStack.removeAt(navBackStack.lastIndex)
                }
            },
            modifier = Modifier.fillMaxSize(),
            entryProvider = entryProvider {
                entry<HomeRoute> { HomeScreen() }
                entry<BrandsRoute> { BrandsScreen() }
                entry<CatalogStackRoute> { CatalogStackScreen(navBackStack = catalogNavBackStack) }
                entry<FittingRoute> { FittingScreen() }
                entry<ConsultantsRoute> { ConsultantsScreen() }
                entry<ProfileRoute> { ProfileScreen() }
            }
        )
    }
}
