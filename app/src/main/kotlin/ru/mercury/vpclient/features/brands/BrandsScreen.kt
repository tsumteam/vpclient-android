package ru.mercury.vpclient.features.brands

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.mercury.vpclient.features.brands.intent.BrandsIntent
import ru.mercury.vpclient.features.brands.model.BrandsModel
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.system.ClientCenterAlignedTopAppBar
import ru.mercury.vpclient.shared.ui.components.system.TopBarActionsState
import ru.mercury.vpclient.shared.ui.components.system.TopBarState
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings

@Composable
fun BrandsScreen(
    viewModel: BrandsViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    BrandsScreenContent(
        state = state,
        dispatch = viewModel::dispatch
    )
}

@Composable
private fun BrandsScreenContent(
    state: BrandsModel,
    dispatch: (BrandsIntent) -> Unit
) {
    SharedScaffold(
        topBar = {
            ClientCenterAlignedTopAppBar(
                state = TopBarState.Title(
                    title = stringResource(ClientStrings.MainTabBrands),
                    showSearch = true,
                    searchClick = { dispatch(BrandsIntent.SearchClick) },
                    actionsState = TopBarActionsState(
                        showCartButton = true,
                        cartText = state.cartText,
                        showCartBadge = state.showCartBadge,
                        cartClick = { dispatch(BrandsIntent.CartClick) },
                        fittingText = state.fittingText,
                        showFittingButton = state.showFittingButton,
                        showFittingBadge = state.showFittingBadge,
                        fittingClick = { dispatch(BrandsIntent.FittingClick) },
                        showMessengerButton = true,
                        showMessengerBadge = state.showMessengerBadge,
                        messengerClick = { dispatch(BrandsIntent.MessengerClick) }
                    )
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
private fun BrandsScreenContentPreview() {
    BrandsScreenContent(
        state = BrandsModel(),
        dispatch = {}
    )
}
