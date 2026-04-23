package ru.mercury.vpclient.features.main.tabs.brands

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.mercury.vpclient.features.main.tabs.brands.intent.BrandsIntent
import ru.mercury.vpclient.features.main.tabs.brands.model.BrandsModel
import ru.mercury.vpclient.shared.data.entity.TopBarState
import ru.mercury.vpclient.shared.ui.components.system.ClientCenterAlignedTopAppBar
import ru.mercury.vpclient.shared.ui.components.system.ClientLazyColumn
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.ClientTheme

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
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ClientCenterAlignedTopAppBar(
                state = TopBarState.Title(stringResource(ClientStrings.MainTabBrands))
            )
        }
    ) { innerPadding ->
        ClientLazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPadding
        ) {
            item {}
        }
    }
}

@FontScalePreviews
@Composable
private fun BrandsScreenContentPreview() {
    ClientTheme {
        BrandsScreenContent(
            state = BrandsModel(),
            dispatch = {}
        )
    }
}
