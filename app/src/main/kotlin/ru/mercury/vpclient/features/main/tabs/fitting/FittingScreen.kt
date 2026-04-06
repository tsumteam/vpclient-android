package ru.mercury.vpclient.features.main.tabs.fitting

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.mercury.vpclient.core.ui.components.system.ClientCenterAlignedTopAppBar
import ru.mercury.vpclient.core.ui.components.system.ClientLazyColumn
import ru.mercury.vpclient.core.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.core.ui.theme.ClientStrings
import ru.mercury.vpclient.core.ui.theme.ClientTheme
import ru.mercury.vpclient.core.ui.theme.medium17
import ru.mercury.vpclient.features.main.tabs.fitting.intent.FittingIntent
import ru.mercury.vpclient.features.main.tabs.fitting.model.FittingModel

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
                title = {
                    Text(
                        text = stringResource(ClientStrings.MainTabFitting),
                        style = MaterialTheme.typography.medium17.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors().copy(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        ClientLazyColumn(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize()
        ) {
            item {}
        }
    }
}

@FontScalePreviews
@Composable
private fun FittingScreenContentPreview() {
    ClientTheme {
        FittingScreenContent(
            state = FittingModel(),
            dispatch = {}
        )
    }
}
