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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import ru.mercury.vpclient.core.ui.theme.ClientIcons
import ru.mercury.vpclient.core.ui.theme.ClientStrings
import ru.mercury.vpclient.core.ui.theme.ClientTheme
import ru.mercury.vpclient.core.ui.theme.ClientTypography
import ru.mercury.vpclient.core.ui.theme.secondary4
import ru.mercury.vpclient.features.main.event.MainTabsEventManager
import ru.mercury.vpclient.features.main.intent.MainIntent
import ru.mercury.vpclient.features.main.model.MainModel
import ru.mercury.vpclient.features.main.tabs.brands.BrandsScreen
import ru.mercury.vpclient.features.main.tabs.brands.navigation.BrandsRoute
import ru.mercury.vpclient.features.main.tabs.catalog.CatalogScreen
import ru.mercury.vpclient.features.main.tabs.catalog.navigation.CatalogRoute
import ru.mercury.vpclient.features.main.tabs.consultants.ConsultantsScreen
import ru.mercury.vpclient.features.main.tabs.consultants.navigation.ConsultantsRoute
import ru.mercury.vpclient.features.main.tabs.fitting.FittingScreen
import ru.mercury.vpclient.features.main.tabs.fitting.navigation.FittingRoute
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
                entry<BrandsRoute> { BrandsScreen() }
                entry<CatalogRoute> { CatalogScreen() }
                entry<FittingRoute> { FittingScreen() }
                entry<ConsultantsRoute> { ConsultantsScreen() }
                entry<ProfileRoute> { ProfileScreen() }
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
                containerColor = Color.White
            ) {
                NavigationBarItem(
                    selected = state.selectedRoute == HomeRoute,
                    onClick = {
                        when {
                            state.selectedRoute != HomeRoute -> dispatch(MainIntent.SelectTab(HomeRoute))
                            else -> navBackStack.setRoot(state.selectedRoute)
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(ClientIcons.Home),
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(ClientStrings.MainTabHome),
                            modifier = Modifier.fillMaxWidth(),
                            style = ClientTypography.Regular_11.copy(textAlign = TextAlign.Center),
                            minLines = 2,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
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
                            else -> navBackStack.setRoot(state.selectedRoute)
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(ClientIcons.Brands),
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
                            style = ClientTypography.Regular_11.copy(textAlign = TextAlign.Center)
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
                    selected = state.selectedRoute == CatalogRoute,
                    onClick = {
                        when {
                            state.selectedRoute != CatalogRoute -> dispatch(MainIntent.SelectTab(CatalogRoute))
                            else -> navBackStack.setRoot(state.selectedRoute)
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(ClientIcons.Catalog),
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(ClientStrings.MainTabCatalog),
                            modifier = Modifier.fillMaxWidth(),
                            style = ClientTypography.Regular_11.copy(textAlign = TextAlign.Center),
                            minLines = 2,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
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
                            else -> navBackStack.setRoot(state.selectedRoute)
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(ClientIcons.Fitting),
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(ClientStrings.MainTabFitting),
                            modifier = Modifier.fillMaxWidth(),
                            style = ClientTypography.Regular_11.copy(textAlign = TextAlign.Center),
                            minLines = 2,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
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
                            else -> navBackStack.setRoot(state.selectedRoute)
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(ClientIcons.Consultants),
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(ClientStrings.MainTabConsultants),
                            modifier = Modifier.fillMaxWidth(),
                            style = ClientTypography.Regular_11.copy(textAlign = TextAlign.Center),
                            minLines = 2,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
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
                    selected = state.selectedRoute == ProfileRoute,
                    onClick = {
                        when {
                            state.selectedRoute != ProfileRoute -> dispatch(MainIntent.SelectTab(ProfileRoute))
                            else -> navBackStack.setRoot(state.selectedRoute)
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(ClientIcons.Profile),
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(ClientStrings.MainTabProfile),
                            modifier = Modifier.fillMaxWidth(),
                            style = ClientTypography.Regular_11.copy(textAlign = TextAlign.Center),
                            minLines = 2,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
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
        }
    }
}

@Preview
@Composable
private fun MainScreenContentPreview() {
    ClientTheme {
        MainScreenContent(
            state = MainModel(),
            dispatch = {},
            navBackStack = rememberNavBackStack(HomeRoute)
        )
    }
}
