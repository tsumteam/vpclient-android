@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.banner

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.banner.event.BannerEvent
import ru.mercury.vpclient.features.banner.intent.BannerIntent
import ru.mercury.vpclient.features.banner.model.BannerModel
import ru.mercury.vpclient.features.banner.navigation.BannerRoute
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.SharedSnackbarHost
import ru.mercury.vpclient.shared.ui.components.cart.CartIconButton
import ru.mercury.vpclient.shared.ui.components.cart.FittingIconButton
import ru.mercury.vpclient.shared.ui.components.cart.MessengerIconButton
import ru.mercury.vpclient.shared.ui.components.profile.ProfileWebView
import ru.mercury.vpclient.shared.ui.icons.ChevronStart24
import ru.mercury.vpclient.shared.ui.icons.Search24
import ru.mercury.vpclient.shared.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper

@Composable
fun BannerScreen(
    route: BannerRoute,
    viewModel: BannerViewModel = hiltViewModel<BannerViewModel, BannerViewModel.Factory>(
        creationCallback = { it.create(route) }
    )
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackbarHostStateError = remember { SnackbarHostState() }

    BannerScreenContent(
        state = state,
        dispatch = viewModel::dispatch,
        snackbarHostStateError = snackbarHostStateError
    )

    ObserveAsEvents(
        flow = viewModel.eventFlow
    ) { event ->
        when (event) {
            is BannerEvent.SnackbarErrorMessage -> {
                snackbarHostStateError.currentSnackbarData?.dismiss()
                scope.launch { snackbarHostStateError.showSnackbar(event.message) }
            }
        }
    }
}

@Composable
private fun BannerScreenContent(
    state: BannerModel,
    dispatch: (BannerIntent) -> Unit,
    snackbarHostStateError: SnackbarHostState
) {
    SharedScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {},
                navigationIcon = {
                    Row {
                        IconButton(
                            onClick = { dispatch(BannerIntent.BackClick) },
                            modifier = Modifier.size(42.dp)
                        ) {
                            Icon(
                                imageVector = ChevronStart24,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        IconButton(
                            onClick = { dispatch(BannerIntent.SearchClick) },
                            modifier = Modifier.size(42.dp)
                        ) {
                            Icon(
                                imageVector = Search24,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                },
                actions = {
                    FittingIconButton(
                        text = "",
                        showBadge = state.isFittingBadgeVisible,
                        onClick = { dispatch(BannerIntent.FittingClick) }
                    )

                    CartIconButton(
                        text = "",
                        showBadge = state.isCartBadgeVisible,
                        onClick = { dispatch(BannerIntent.CartClick) }
                    )

                    MessengerIconButton(
                        showBadge = state.isMessengerBadgeVisible,
                        onClick = { dispatch(BannerIntent.MessengerClick) }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        snackbarHost = {
            SharedSnackbarHost(
                hostState = snackbarHostStateError,
                containerColor = MaterialTheme.colorScheme.error
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
private fun BannerScreenContentPreview(
    @PreviewParameter(BannerModelPreviewParameterProvider::class) state: BannerModel
) {
    BannerScreenContent(
        state = state,
        dispatch = {},
        snackbarHostStateError = remember { SnackbarHostState() }
    )
}

private class BannerModelPreviewParameterProvider: PreviewParameterProvider<BannerModel> {
    override val values: Sequence<BannerModel> = sequenceOf(
        BannerModel()
    )
}
