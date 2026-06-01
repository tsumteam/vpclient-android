package ru.mercury.vpclient.features.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.mercury.vpclient.features.profile.intent.ProfileIntent
import ru.mercury.vpclient.features.profile.model.ProfileModel
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.system.ClientCenterAlignedTopAppBar
import ru.mercury.vpclient.shared.ui.components.system.TopBarActionsState
import ru.mercury.vpclient.shared.ui.components.system.TopBarState
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.livretRegular15

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
    SharedScaffold(
        topBar = {
            ClientCenterAlignedTopAppBar(
                state = TopBarState.Title(
                    title = stringResource(ClientStrings.ProfileTitle),
                    actionsState = TopBarActionsState(
                        showCartButton = true,
                        cartText = state.cartText,
                        showCartBadge = state.showCartBadge,
                        cartClick = { dispatch(ProfileIntent.CartClick) },
                        fittingText = state.fittingText,
                        showFittingButton = state.showFittingButton,
                        showFittingBadge = state.showFittingBadge,
                        fittingClick = { dispatch(ProfileIntent.FittingClick) },
                        showMessengerButton = true,
                        showMessengerBadge = state.showMessengerBadge,
                        messengerClick = { dispatch(ProfileIntent.MessengerClick) }
                    )
                )
            )
        },
        bottomBar = {
            OutlinedButton(
                onClick = { dispatch(ProfileIntent.Logout) },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = !state.isLogoutLoading,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    disabledContainerColor = MaterialTheme.colorScheme.background,
                    disabledContentColor = MaterialTheme.colorScheme.onBackground
                ),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                when {
                    state.isLogoutLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onBackground,
                            strokeWidth = 2.dp
                        )
                    }
                    else -> {
                        Text(
                            text = stringResource(ClientStrings.ProfileLogout),
                            style = MaterialTheme.typography.livretRegular15.copy(
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
            }
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
private fun ProfileScreenContentPreview(
) {
    ProfileScreenContent(
        state = ProfileModel(),
        dispatch = {}
    )
}
