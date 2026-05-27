package ru.mercury.vpclient.features.main.tabs.fitting

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.mercury.vpclient.features.main.tabs.fitting.intent.FittingIntent
import ru.mercury.vpclient.features.main.tabs.fitting.model.FittingModel
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.system.ClientCenterAlignedTopAppBar
import ru.mercury.vpclient.shared.ui.components.system.TopBarState
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings

@Composable
fun FittingScreen(
    viewModel: FittingViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    FittingScreenContent(
        state = state,
        dispatch = viewModel::dispatch
    )
}

@Composable
private fun FittingScreenContent(
    state: FittingModel,
    dispatch: (FittingIntent) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ClientCenterAlignedTopAppBar(
                state = TopBarState.Title(stringResource(ClientStrings.MainTabFitting))
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
private fun FittingScreenContentPreview() {
    FittingScreenContent(
        state = FittingModel(),
        dispatch = {}
    )
}
