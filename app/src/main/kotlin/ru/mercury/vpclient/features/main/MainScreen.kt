package ru.mercury.vpclient.features.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
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
import ru.mercury.vpclient.features.brand_root.BrandRootScreen
import ru.mercury.vpclient.features.brand_root.navigation.BrandRootRoute
import ru.mercury.vpclient.features.brands.navigation.BrandsRoute
import ru.mercury.vpclient.features.catalog.navigation.CatalogRoute
import ru.mercury.vpclient.features.catalog_root.CatalogRootScreen
import ru.mercury.vpclient.features.catalog_root.navigation.CatalogRootRoute
import ru.mercury.vpclient.features.compilations.CompilationsScreen
import ru.mercury.vpclient.features.compilations.navigation.CompilationsRoute
import ru.mercury.vpclient.features.consultants.ConsultantsScreen
import ru.mercury.vpclient.features.consultants.navigation.ConsultantsRoute
import ru.mercury.vpclient.features.details.navigation.DetailsRoute
import ru.mercury.vpclient.features.home.navigation.HomeRoute
import ru.mercury.vpclient.features.home_root.HomeRootScreen
import ru.mercury.vpclient.features.home_root.navigation.HomeRootRoute
import ru.mercury.vpclient.features.main.event.MainTabsEventManager
import ru.mercury.vpclient.features.main.intent.MainIntent
import ru.mercury.vpclient.features.main.model.MainModel
import ru.mercury.vpclient.features.main.navigation.MainRoute
import ru.mercury.vpclient.features.profile.navigation.ProfileRoute
import ru.mercury.vpclient.features.profile_root.ProfileRootScreen
import ru.mercury.vpclient.features.profile_root.navigation.ProfileRootRoute
import ru.mercury.vpclient.shared.ui.components.consultants.ConsultantsTabIcon
import ru.mercury.vpclient.shared.ui.components.consultants.ConsultantsTabIconState
import ru.mercury.vpclient.shared.ui.components.system.ClientNavDisplay
import ru.mercury.vpclient.shared.ui.icons.Brands24
import ru.mercury.vpclient.shared.ui.icons.Catalog24
import ru.mercury.vpclient.shared.ui.icons.Fitting24
import ru.mercury.vpclient.shared.ui.icons.Home24
import ru.mercury.vpclient.shared.ui.icons.Profile24
import ru.mercury.vpclient.shared.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.regular11

@Composable
fun MainScreen(
    route: MainRoute = MainRoute(),
    viewModel: MainViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val mainBackStack: NavBackStack<NavKey> = rememberNavBackStack(state.selectedRoute)
    val homeBackStack: NavBackStack<NavKey> = rememberNavBackStack(HomeRoute)
    val brandBackStack: NavBackStack<NavKey> = rememberNavBackStack(BrandsRoute)
    val catalogBackStack: NavBackStack<NavKey> = rememberNavBackStack(CatalogRoute)
    val profileBackStack: NavBackStack<NavKey> = rememberNavBackStack(ProfileRoute)

    LaunchedEffect(state.selectedRoute) {
        mainBackStack.clear()
        mainBackStack.add(state.selectedRoute)
    }

    LaunchedEffect(route.selectedTab) {
        when (route.selectedTab) {
            MainRoute.CATALOG_TAB -> viewModel.dispatch(MainIntent.SelectTab(CatalogRootRoute))
            MainRoute.FITTING_TAB -> viewModel.dispatch(MainIntent.SelectTab(CompilationsRoute))
        }
    }

    MainScreenContent(
        state = state,
        dispatch = viewModel::dispatch,
        navBackStack = mainBackStack,
        homeNavBackStack = homeBackStack,
        brandNavBackStack = brandBackStack,
        catalogNavBackStack = catalogBackStack,
        profileNavBackStack = profileBackStack
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
    homeNavBackStack: NavBackStack<NavKey>,
    brandNavBackStack: NavBackStack<NavKey>,
    catalogNavBackStack: NavBackStack<NavKey>,
    profileNavBackStack: NavBackStack<NavKey>
) {
    val dividerColor = MaterialTheme.colorScheme.outlineVariant
    val navigationSuiteScaffoldState = rememberNavigationSuiteScaffoldState()
    val isBottomBarVisible by remember(state.selectedRoute, homeNavBackStack, brandNavBackStack, catalogNavBackStack) {
        derivedStateOf {
            when {
                state.selectedRoute == HomeRootRoute -> homeNavBackStack.lastOrNull() !is DetailsRoute
                state.selectedRoute == BrandRootRoute -> brandNavBackStack.lastOrNull() !is DetailsRoute
                state.selectedRoute != CatalogRootRoute -> true
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
                    selected = state.selectedRoute == HomeRootRoute,
                    onClick = {
                        when {
                            state.selectedRoute != HomeRootRoute -> {
                                dispatch(MainIntent.SelectTab(HomeRootRoute))
                            }
                            else -> {
                                navBackStack.clear()
                                navBackStack.add(state.selectedRoute)
                                homeNavBackStack.clear()
                                homeNavBackStack.add(HomeRoute)
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Home24,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
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
                        unselectedIconColor = MaterialTheme.colorScheme.outline,
                        unselectedTextColor = MaterialTheme.colorScheme.outline,
                        selectedIndicatorColor = Color.Transparent
                    )
                )

                NavigationBarItem(
                    selected = state.selectedRoute == BrandRootRoute,
                    onClick = {
                        when {
                            state.selectedRoute != BrandRootRoute -> {
                                dispatch(MainIntent.SelectTab(BrandRootRoute))
                            }
                            else -> {
                                navBackStack.clear()
                                navBackStack.add(state.selectedRoute)
                                brandNavBackStack.clear()
                                brandNavBackStack.add(BrandsRoute)
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Brands24,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
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
                        unselectedIconColor = MaterialTheme.colorScheme.outline,
                        unselectedTextColor = MaterialTheme.colorScheme.outline,
                        selectedIndicatorColor = Color.Transparent
                    )
                )

                NavigationBarItem(
                    selected = state.selectedRoute == CatalogRootRoute,
                    onClick = {
                        when {
                            state.selectedRoute != CatalogRootRoute -> {
                                dispatch(MainIntent.SelectTab(CatalogRootRoute))
                            }
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
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
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
                        unselectedIconColor = MaterialTheme.colorScheme.outline,
                        unselectedTextColor = MaterialTheme.colorScheme.outline,
                        selectedIndicatorColor = Color.Transparent
                    )
                )

                NavigationBarItem(
                    selected = state.selectedRoute == CompilationsRoute,
                    onClick = {
                        when {
                            state.selectedRoute != CompilationsRoute -> {
                                dispatch(MainIntent.SelectTab(CompilationsRoute))
                            }
                            else -> {
                                navBackStack.clear()
                                navBackStack.add(state.selectedRoute)
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Fitting24,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
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
                        unselectedIconColor = MaterialTheme.colorScheme.outline,
                        unselectedTextColor = MaterialTheme.colorScheme.outline,
                        selectedIndicatorColor = Color.Transparent
                    )
                )

                NavigationBarItem(
                    selected = state.selectedRoute == ConsultantsRoute,
                    onClick = {
                        when {
                            state.selectedRoute != ConsultantsRoute -> {
                                dispatch(MainIntent.SelectTab(ConsultantsRoute))
                            }
                            else -> {
                                navBackStack.clear()
                                navBackStack.add(state.selectedRoute)
                            }
                        }
                    },
                    icon = {
                        ConsultantsTabIcon(
                            state = ConsultantsTabIconState(
                                selected = state.selectedRoute == ConsultantsRoute,
                                badgeText = state.consultantsBadgeText
                            )
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
                        unselectedTextColor = MaterialTheme.colorScheme.outline,
                        selectedIndicatorColor = Color.Transparent
                    )
                )

                NavigationBarItem(
                    selected = state.selectedRoute == ProfileRootRoute,
                    onClick = {
                        when {
                            state.selectedRoute != ProfileRootRoute -> {
                                dispatch(MainIntent.SelectTab(ProfileRootRoute))
                            }
                            else -> {
                                navBackStack.clear()
                                navBackStack.add(state.selectedRoute)
                                profileNavBackStack.clear()
                                profileNavBackStack.add(ProfileRoute)
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Profile24,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
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
                        unselectedIconColor = MaterialTheme.colorScheme.outline,
                        unselectedTextColor = MaterialTheme.colorScheme.outline,
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
                entry<HomeRootRoute> { HomeRootScreen(navBackStack = homeNavBackStack) }
                entry<BrandRootRoute> { BrandRootScreen(navBackStack = brandNavBackStack) }
                entry<CatalogRootRoute> { CatalogRootScreen(navBackStack = catalogNavBackStack) }
                entry<CompilationsRoute> { CompilationsScreen() }
                entry<ConsultantsRoute> { ConsultantsScreen() }
                entry<ProfileRootRoute> { ProfileRootScreen(navBackStack = profileNavBackStack) }
            }
        )
    }
}
