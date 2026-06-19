@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.profile_qr

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.mercury.vpclient.features.profile_qr.intent.ProfileQrIntent
import ru.mercury.vpclient.features.profile_qr.model.ProfileQrModel
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.profile.ProfileQrImage
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium18
import ru.mercury.vpclient.shared.ui.theme.regular15

@Composable
fun ProfileQrScreen(
    viewModel: ProfileQrViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    ProfileQrScreenContent(
        state = state,
        dispatch = viewModel::dispatch
    )
}

@Composable
private fun ProfileQrScreenContent(
    state: ProfileQrModel,
    dispatch: (ProfileQrIntent) -> Unit
) {
    SharedScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(ClientStrings.ProfileQrTitle),
                        style = MaterialTheme.typography.medium18.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { dispatch(ProfileQrIntent.CloseClick) }
                    ) {
                        Icon(
                            imageVector = Close24,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        SharedLazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPadding + PaddingValues(start = 68.dp, top = 48.dp, end = 68.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                ProfileQrImage(
                    qrCode = state.qrCode,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1F)
                )
            }
            item {
                Text(
                    text = stringResource(ClientStrings.ProfileQrDescription),
                    style = MaterialTheme.typography.regular15.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 19.sp,
                        letterSpacing = .2.sp,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun ProfileQrScreenContentPreview() {
    ProfileQrScreenContent(
        state = ProfileQrModel(
            qrCode = "1234567890"
        ),
        dispatch = {}
    )
}
