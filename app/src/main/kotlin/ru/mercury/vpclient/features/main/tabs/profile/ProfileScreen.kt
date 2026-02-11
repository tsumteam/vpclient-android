package ru.mercury.vpclient.features.main.tabs.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.mercury.vpclient.core.persistence.database.entity.ClientEntity
import ru.mercury.vpclient.core.ui.components.ClientButton
import ru.mercury.vpclient.core.ui.components.ClientLazyColumn
import ru.mercury.vpclient.core.ui.theme.ClientStrings
import ru.mercury.vpclient.core.ui.theme.ClientTheme
import ru.mercury.vpclient.core.ui.theme.ClientTypography
import ru.mercury.vpclient.core.ui.theme.divider
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(MaterialTheme.colorScheme.surface),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(
                    modifier = Modifier.height(46.dp)
                )

                Text(
                    text = state.clientEntity.name,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 40.dp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 5,
                    style = ClientTypography.Regular_16_OnBackground.copy(textAlign = TextAlign.Center)
                )
            }
        }
    ) { innerPadding ->
        ClientLazyColumn(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize()
        ) {
            item {
                Spacer(
                    modifier = Modifier.height(64.dp)
                )
            }
            item {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth().height(1.dp),
                    color = MaterialTheme.colorScheme.divider
                )
            }
            item {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth().height(1.dp),
                    color = MaterialTheme.colorScheme.divider
                )
            }
            item {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth().height(1.dp),
                    color = MaterialTheme.colorScheme.divider
                )
            }
            item {
                ClientButton(
                    onClick = { dispatch(ProfileIntent.ConfirmLogout) },
                    text = stringResource(ClientStrings.ProfileLogout),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 24.dp)
                )
            }
        }
    }
}

@Preview
@Preview(fontScale = 1.5F)
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
