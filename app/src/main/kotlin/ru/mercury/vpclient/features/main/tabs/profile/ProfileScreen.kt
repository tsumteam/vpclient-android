package ru.mercury.vpclient.features.main.tabs.profile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.mercury.vpclient.core.persistence.database.entity.ClientEntity
import ru.mercury.vpclient.core.ui.components.ClientButton
import ru.mercury.vpclient.core.ui.components.ClientCenterAlignedTopAppBar
import ru.mercury.vpclient.core.ui.components.ClientLazyColumn
import ru.mercury.vpclient.core.ui.theme.ClientStrings
import ru.mercury.vpclient.core.ui.theme.ClientTheme
import ru.mercury.vpclient.core.ui.theme.medium17
import ru.mercury.vpclient.core.ui.theme.onBackground
import ru.mercury.vpclient.features.main.tabs.profile.intent.ProfileIntent
import ru.mercury.vpclient.features.main.tabs.profile.model.ProfileModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    ProfileScreenContent(
        state = state,
        dispatch = viewModel::dispatch
    )
}

@Composable
private fun ProfileScreenContent(
    state: ProfileModel,
    dispatch: (ProfileIntent) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ClientCenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(ClientStrings.ProfileTitle),
                        style = MaterialTheme.typography.medium17.copy(textAlign = TextAlign.Center).onBackground()
                    )
                }
            )
        },
        bottomBar = {
            ClientButton(
                onClick = { dispatch(ProfileIntent.ConfirmLogout) },
                text = stringResource(ClientStrings.ProfileLogout),
                modifier = Modifier.padding(horizontal = 16.dp)
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

@Preview
@Preview(fontScale = 3F)
@Composable
private fun ProfileScreenContentPreview(
) {
    ClientTheme {
        ProfileScreenContent(
            state = ProfileModel(
                clientEntity = ClientEntity(
                    phone = "+79000000000",
                    name = "Иван Иванов"
                )
            ),
            dispatch = {}
        )
    }
}
