package ru.mercury.vpclient.features.main.tabs.consultants

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.mercury.vpclient.core.ui.components.ClientCenterAlignedTopAppBar
import ru.mercury.vpclient.core.ui.components.ClientLazyColumn
import ru.mercury.vpclient.core.ui.components.ConsultantBox
import ru.mercury.vpclient.core.ui.theme.ClientStrings
import ru.mercury.vpclient.core.ui.theme.medium17
import ru.mercury.vpclient.core.ui.theme.onBackground
import ru.mercury.vpclient.features.main.tabs.consultants.intent.ConsultantsIntent
import ru.mercury.vpclient.features.main.tabs.consultants.model.ConsultantsModel

@Composable
fun ConsultantsScreen(
    viewModel: ConsultantsViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    ConsultantsScreenContent(
        state = state,
        dispatch = viewModel::dispatch
    )
}

@Composable
private fun ConsultantsScreenContent(
    state: ConsultantsModel,
    dispatch: (ConsultantsIntent) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ClientCenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(ClientStrings.ConsultantsTitle),
                        style = MaterialTheme.typography.medium17.copy(textAlign = TextAlign.Center).onBackground()
                    )
                }
            )
        }
    ) { innerPadding ->
        ClientLazyColumn(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = innerPadding.calculateBottomPadding())
        ) {
            items(
                items = state.consultants,
                key = { consultant -> consultant.id }
            ) { consultant ->
                ConsultantBox(
                    name = consultant.name,
                    avatarUrl = consultant.avatarUrl,
                    actions = consultant.actions,
                    isActive = consultant.isActive,
                    onActionClick = {},
                    onActiveClick = { dispatch(ConsultantsIntent.SetActiveConsultant(consultant.id)) },
                    onClick = { dispatch(ConsultantsIntent.SetActiveConsultant(consultant.id)) }
                )
            }
        }
    }
}
