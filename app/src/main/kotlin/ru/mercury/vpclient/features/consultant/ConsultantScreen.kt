package ru.mercury.vpclient.features.consultant

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import ru.mercury.vpclient.core.ui.components.ClientLazyColumn
import ru.mercury.vpclient.core.ui.components.ClientTopAppBar
import ru.mercury.vpclient.core.ui.components.ConsultantActionsRow
import ru.mercury.vpclient.core.ui.icons.ChevronStart24
import ru.mercury.vpclient.core.ui.theme.ClientTheme
import ru.mercury.vpclient.core.ui.theme.livretMedium21
import ru.mercury.vpclient.core.ui.theme.medium21
import ru.mercury.vpclient.core.ui.theme.onBackground
import ru.mercury.vpclient.core.ui.theme.regular16
import ru.mercury.vpclient.core.ui.theme.secondary
import ru.mercury.vpclient.features.consultant.intent.ConsultantIntent
import ru.mercury.vpclient.features.consultant.model.ConsultantModel

@Composable
fun ConsultantScreen(
    consultantId: String,
    viewModel: ConsultantViewModel = hiltViewModel()
) {
    LaunchedEffect(consultantId) {
        viewModel.dispatch(ConsultantIntent.LoadConsultant(consultantId))
    }

    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    ConsultantScreenContent(
        state = state,
        dispatch = viewModel::dispatch
    )
}

@Composable
private fun ConsultantScreenContent(
    state: ConsultantModel,
    dispatch: (ConsultantIntent) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ClientTopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { dispatch(ConsultantIntent.BackClick) }) {
                        Icon(
                            imageVector = ChevronStart24,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        ClientLazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
            contentPadding = PaddingValues(bottom = innerPadding.calculateBottomPadding())
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = state.consultant.avatarUrl,
                        contentDescription = state.consultant.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(218.dp)
                            .clip(CircleShape)
                    )
                }
            }
            item {
                Text(
                    text = state.consultant.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp, start = 16.dp, end = 16.dp),
                    style = MaterialTheme.typography.medium21.copy(textAlign = TextAlign.Center).onBackground()
                )
            }
            item {
                Text(
                    text = state.consultant.workplace,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                    style = MaterialTheme.typography.livretMedium21.copy(textAlign = TextAlign.Center).onBackground()
                )
            }
            item {
                Text(
                    text = state.consultant.storeName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                    style = MaterialTheme.typography.regular16.copy(textAlign = TextAlign.Center).secondary()
                )
            }
            item {
                ConsultantActionsRow(
                    actions = state.consultant.actions,
                    onActionClick = {},
                    modifier = Modifier.padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ConsultantScreenPreview() {
    ClientTheme {
        ConsultantScreenContent(
            state = ConsultantModel(
                consultant = ru.mercury.vpclient.features.main.tabs.consultants.api.ConsultantsMockApi
                    .getConsultants()
                    .first()
            ),
            dispatch = {}
        )
    }
}
