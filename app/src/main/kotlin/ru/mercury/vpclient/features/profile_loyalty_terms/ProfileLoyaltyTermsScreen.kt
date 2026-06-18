@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.profile_loyalty_terms

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.mercury.vpclient.features.profile_loyalty_terms.intent.ProfileLoyaltyTermsIntent
import ru.mercury.vpclient.features.profile_loyalty_terms.model.ProfileLoyaltyTermsModel
import ru.mercury.vpclient.features.profile_loyalty_terms.navigation.ProfileLoyaltyTermsRoute
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.profile.ProfileWebView
import ru.mercury.vpclient.shared.ui.icons.ChevronStart24
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium18

@Composable
fun ProfileLoyaltyTermsScreen(
    route: ProfileLoyaltyTermsRoute,
    viewModel: ProfileLoyaltyTermsViewModel = hiltViewModel<ProfileLoyaltyTermsViewModel, ProfileLoyaltyTermsViewModel.Factory>(
        creationCallback = { it.create(route) }
    )
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    ProfileLoyaltyTermsScreenContent(
        state = state,
        dispatch = viewModel::dispatch
    )
}

@Composable
private fun ProfileLoyaltyTermsScreenContent(
    state: ProfileLoyaltyTermsModel,
    dispatch: (ProfileLoyaltyTermsIntent) -> Unit
) {
    SharedScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(ClientStrings.ProfileLoyaltyInfoTermsOfUse),
                        style = MaterialTheme.typography.medium18.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { dispatch(ProfileLoyaltyTermsIntent.BackClick) }
                    ) {
                        Icon(
                            imageVector = ChevronStart24,
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
        ProfileWebView(
            url = state.url,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun ProfileLoyaltyTermsScreenContentPreview() {
    ProfileLoyaltyTermsScreenContent(
        state = ProfileLoyaltyTermsModel(),
        dispatch = {}
    )
}
