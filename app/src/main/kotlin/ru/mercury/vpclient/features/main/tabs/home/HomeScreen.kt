package ru.mercury.vpclient.features.main.tabs.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.mercury.vpclient.features.main.tabs.home.intent.HomeIntent
import ru.mercury.vpclient.features.main.tabs.home.model.HomeModel
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.system.ClientCenterAlignedTopAppBar
import ru.mercury.vpclient.shared.ui.components.system.TopBarActionsState
import ru.mercury.vpclient.shared.ui.components.system.TopBarState
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    HomeScreenContent(
        state = state,
        dispatch = viewModel::dispatch
    )
}

@Composable
private fun HomeScreenContent(
    state: HomeModel,
    dispatch: (HomeIntent) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ClientCenterAlignedTopAppBar(
                state = TopBarState.Home(
                    actionsState = TopBarActionsState(
                        showCartButton = true,
                        cartText = state.cartText,
                        showCartBadge = state.showCartBadge,
                        cartClick = { dispatch(HomeIntent.CartClick) },
                        fittingText = state.fittingText,
                        showFittingButton = state.showFittingButton,
                        showFittingBadge = state.showFittingBadge,
                        fittingClick = { dispatch(HomeIntent.FittingClick) },
                        showMessengerButton = true,
                        showMessengerBadge = state.showMessengerBadge,
                        messengerClick = { dispatch(HomeIntent.MessengerClick) }
                    ),
                    searchClick = { dispatch(HomeIntent.SearchClick) }
                )
            )
        }
    ) { innerPadding ->
        SharedLazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPadding
        ) {
            item {}
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@FontScalePreviews
@Composable
private fun HomeScreenContentPreview() {
    HomeScreenContent(
        state = HomeModel(),
        dispatch = {}
    )
}
