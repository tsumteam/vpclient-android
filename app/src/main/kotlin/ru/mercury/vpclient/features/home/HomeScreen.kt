@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.mercury.vpclient.features.home.intent.HomeIntent
import ru.mercury.vpclient.features.home.model.HomeModel
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.cart.CartIconButton
import ru.mercury.vpclient.shared.ui.components.cart.FittingIconButton
import ru.mercury.vpclient.shared.ui.components.cart.MessengerIconButton
import ru.mercury.vpclient.shared.ui.icons.Logo82
import ru.mercury.vpclient.shared.ui.icons.Search24
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
    SharedScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Icon(
                        imageVector = Logo82,
                        contentDescription = null,
                        modifier = Modifier.size(82.dp, 57.dp),
                        tint = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { dispatch(HomeIntent.SearchClick) },
                        modifier = Modifier.size(42.dp)
                    ) {
                        Icon(
                            imageVector = Search24,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                actions = {
                    if (state.showFittingButton) {
                        FittingIconButton(
                            text = state.fittingText,
                            showBadge = state.showFittingBadge,
                            onClick = { dispatch(HomeIntent.FittingClick) }
                        )
                    }

                    CartIconButton(
                        text = state.cartText,
                        showBadge = state.showCartBadge,
                        onClick = { dispatch(HomeIntent.CartClick) }
                    )

                    MessengerIconButton(
                        showBadge = state.showMessengerBadge,
                        onClick = { dispatch(HomeIntent.MessengerClick) }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
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
@Preview
@Composable
private fun HomeScreenContentPreview() {
    HomeScreenContent(
        state = HomeModel(),
        dispatch = {}
    )
}
