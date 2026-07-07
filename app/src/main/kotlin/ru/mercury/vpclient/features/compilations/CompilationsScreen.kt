@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.compilations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.compilation_chat_sheet.CompilationChatSheet
import ru.mercury.vpclient.features.compilation_chat_sheet.intent.CompilationChatIntent
import ru.mercury.vpclient.features.compilation_chat_sheet.model.CompilationChatModel
import ru.mercury.vpclient.features.compilations.event.CompilationsEvent
import ru.mercury.vpclient.features.compilations.intent.CompilationsIntent
import ru.mercury.vpclient.features.compilations.model.CompilationsModel
import ru.mercury.vpclient.shared.data.persistence.database.entity.CompilationEntity
import ru.mercury.vpclient.shared.ui.PlaceholderHighlight
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedPullToRefreshBox
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.SharedSnackbarHost
import ru.mercury.vpclient.shared.ui.components.cart.CartIconButton
import ru.mercury.vpclient.shared.ui.components.cart.FittingIconButton
import ru.mercury.vpclient.shared.ui.components.cart.MessengerIconButton
import ru.mercury.vpclient.shared.ui.components.compilations.CompilationCard
import ru.mercury.vpclient.shared.ui.components.compilations.CompilationCardState
import ru.mercury.vpclient.shared.ui.icons.Empty210
import ru.mercury.vpclient.shared.ui.icons.Search24
import ru.mercury.vpclient.shared.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.shared.ui.placeholder
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.shimmer
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.divider
import ru.mercury.vpclient.shared.ui.theme.medium18
import ru.mercury.vpclient.shared.ui.theme.regular14

@Composable
fun CompilationsScreen(
    viewModel: CompilationsViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackbarHostStateError = remember { SnackbarHostState() }

    CompilationsScreenContent(
        state = state,
        dispatch = viewModel::dispatch,
        snackbarHostStateError = snackbarHostStateError
    )

    if (state.isCompilationChatSheetVisible) {
        CompilationChatSheet(
            state = CompilationChatModel(
                compilationEntity = state.selectedCompilationChatEntity
            ),
            dispatch = { intent ->
                when (intent) {
                    is CompilationChatIntent.DismissRequest -> {
                        viewModel.dispatch(CompilationsIntent.HideCompilationChatSheet)
                    }
                    is CompilationChatIntent.CommentChange -> Unit
                    is CompilationChatIntent.SendClick -> {
                        viewModel.dispatch(CompilationsIntent.CompilationChatSendClick(intent.comment))
                    }
                }
            }
        )
    }

    ObserveAsEvents(
        flow = viewModel.eventFlow
    ) { event ->
        when (event) {
            is CompilationsEvent.SnackbarErrorMessage -> {
                snackbarHostStateError.currentSnackbarData?.dismiss()
                scope.launch { snackbarHostStateError.showSnackbar(event.message) }
            }
        }
    }
}

@Composable
private fun CompilationsScreenContent(
    state: CompilationsModel,
    dispatch: (CompilationsIntent) -> Unit,
    snackbarHostStateError: SnackbarHostState
) {
    var openedSwipeKey by remember { mutableStateOf<String?>(null) }
    val pullToRefreshState = rememberPullToRefreshState()

    SharedScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(ClientStrings.MainTabFitting),
                        style = MaterialTheme.typography.medium18
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { dispatch(CompilationsIntent.SearchClick) }
                    ) {
                        Icon(
                            imageVector = Search24,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                actions = {
                    if (state.isFittingButtonVisible) {
                        FittingIconButton(
                            text = state.fittingText,
                            showBadge = state.isFittingBadgeVisible,
                            onClick = { dispatch(CompilationsIntent.FittingClick) }
                        )
                    }

                    CartIconButton(
                        text = state.cartText,
                        showBadge = state.isCartBadgeVisible,
                        onClick = { dispatch(CompilationsIntent.CartClick) }
                    )

                    MessengerIconButton(
                        showBadge = state.isMessengerBadgeVisible,
                        onClick = { dispatch(CompilationsIntent.MessengerClick) }
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
        SharedPullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { dispatch(CompilationsIntent.LoadCompilationsClient) },
            modifier = Modifier.fillMaxSize(),
            enabled = !state.isInitialLoading,
            state = pullToRefreshState,
            indicator = {
                PullToRefreshDefaults.Indicator(
                    state = pullToRefreshState,
                    isRefreshing = state.isRefreshing,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = innerPadding.calculateTopPadding())
                )
            }
        ) {
            when {
                state.isInitialLoading -> {
                    SharedLazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = innerPadding,
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                        userScrollEnabled = false
                    ) {
                        items(
                            count = 5
                        ) {
                            Spacer(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .fillMaxWidth()
                                    .height(144.dp)
                                    .placeholder(
                                        visible = true,
                                        highlight = PlaceholderHighlight.shimmer(),
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                            )
                        }
                    }
                }
                state.isEmptyVisible -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
                    ) {
                        Icon(
                            imageVector = Empty210,
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(width = 210.dp, height = 111.dp)
                        )

                        Text(
                            text = stringResource(ClientStrings.FittingCompilationsEmptyMessage),
                            modifier = Modifier
                                .padding(horizontal = 32.dp)
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.regular14.copy(
                                color = MaterialTheme.colorScheme.secondary,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
                else -> {
                    SharedLazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = innerPadding + PaddingValues(bottom = 16.dp)
                    ) {
                        items(
                            count = state.compilationEntities.size,
                            key = { index -> state.compilationEntities[index].id }
                        ) { index ->
                            val compilation = state.compilationEntities[index]

                            CompilationCard(
                                state = CompilationCardState(
                                    entity = compilation,
                                    onClick = { dispatch(CompilationsIntent.CompilationClick(compilation.id)) },
                                    onChatClick = { dispatch(CompilationsIntent.CompilationChatClick(compilation.id)) },
                                    swipeKey = compilation.id.toString(),
                                    openedSwipeKey = openedSwipeKey,
                                    onSwipeOpen = { key -> openedSwipeKey = key }
                                )
                            )

                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.divider,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun CompilationsScreenContentPreview(
    @PreviewParameter(CompilationsModelPreviewParameterProvider::class) state: CompilationsModel
) {
    CompilationsScreenContent(
        state = state,
        dispatch = {},
        snackbarHostStateError = remember { SnackbarHostState() }
    )
}

private class CompilationsModelPreviewParameterProvider: PreviewParameterProvider<CompilationsModel> {

    private val previewCompilations = listOf(
        CompilationEntity(
            id = 1,
            position = 0,
            collageUrl = "",
            photoUrl = "",
            name = "Подборка от стилиста",
            description = "",
            createDate = "2026-07-11",
            looksQty = 5,
            lookProductsQty = 36,
            badge = 1,
            isStatsAvailable = false
        ),
        CompilationEntity(
            id = 2,
            position = 1,
            collageUrl = "",
            photoUrl = "",
            name = "Повседневные образы",
            description = "",
            createDate = "2026-07-10",
            looksQty = 3,
            lookProductsQty = 18,
            badge = 0,
            isStatsAvailable = false
        )
    )

    override val values: Sequence<CompilationsModel> = sequenceOf(
        CompilationsModel(
            cartCount = 37,
            fittingCount = 3,
            compilationEntities = previewCompilations
        ),
        CompilationsModel(
            cartCount = 37,
            fittingCount = 3,
            compilationsClientJob = Job()
        ),
        CompilationsModel(
            cartCount = 37,
            fittingCount = 3
        )
    )
}
