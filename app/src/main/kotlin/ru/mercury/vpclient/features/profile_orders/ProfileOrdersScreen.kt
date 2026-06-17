@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.profile_orders

import android.content.ClipData
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.profile_orders.event.ProfileOrdersEvent
import ru.mercury.vpclient.features.profile_orders.intent.ProfileOrdersIntent
import ru.mercury.vpclient.features.profile_orders.model.ProfileOrdersModel
import ru.mercury.vpclient.features.profile_orders.model.profileOrdersPreviewItems
import ru.mercury.vpclient.shared.ui.PlaceholderHighlight
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedPullToRefreshBox
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.cart.CartIconButton
import ru.mercury.vpclient.shared.ui.components.cart.FittingIconButton
import ru.mercury.vpclient.shared.ui.components.cart.MessengerIconButton
import ru.mercury.vpclient.shared.ui.components.profile.ProfileOrderItem
import ru.mercury.vpclient.shared.ui.components.profile.ProfileOrderItemState
import ru.mercury.vpclient.shared.ui.icons.ChevronStart24
import ru.mercury.vpclient.shared.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.shared.ui.ktx.isRefreshLoading
import ru.mercury.vpclient.shared.ui.placeholder
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.shimmer
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.divider
import ru.mercury.vpclient.shared.ui.theme.medium18

@Composable
fun ProfileOrdersScreen(
    viewModel: ProfileOrdersViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val pagingItems = viewModel.ordersPagingFlow.collectAsLazyPagingItems()
    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()

    ProfileOrdersScreenContent(
        state = state,
        pagingItems = pagingItems,
        dispatch = viewModel::dispatch,
        copyOrderNumber = { orderNumber ->
            scope.launch {
                clipboard.setClipEntry(ClipEntry(ClipData.newPlainText(null, orderNumber)))
            }
        }
    )

    ObserveAsEvents(
        flow = viewModel.eventFlow
    ) { event ->
        when (event) {
            is ProfileOrdersEvent.RefreshOrders -> pagingItems.refresh()
        }
    }

    LaunchedEffect(state.isRefreshing) {
        if (!state.isRefreshing) return@LaunchedEffect
        snapshotFlow { pagingItems.isRefreshLoading }
            .distinctUntilChanged()
            .dropWhile { isLoading -> !isLoading }
            .first { isLoading -> !isLoading }
        viewModel.dispatch(ProfileOrdersIntent.RefreshCompleted)
    }
}

@Composable
private fun ProfileOrdersScreenContent(
    state: ProfileOrdersModel,
    pagingItems: LazyPagingItems<ProfileOrderItemState>,
    dispatch: (ProfileOrdersIntent) -> Unit,
    copyOrderNumber: (String) -> Unit
) {
    val pullToRefreshState = rememberPullToRefreshState()

    SharedScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(ClientStrings.ProfilePurchases),
                        style = MaterialTheme.typography.medium18.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { dispatch(ProfileOrdersIntent.BackClick) }
                    ) {
                        Icon(
                            imageVector = ChevronStart24,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
                    if (state.showFittingButton) {
                        FittingIconButton(
                            text = state.fittingText,
                            showBadge = state.showFittingBadge,
                            onClick = { dispatch(ProfileOrdersIntent.FittingClick) }
                        )
                    }

                    CartIconButton(
                        text = state.cartText,
                        showBadge = state.showCartBadge,
                        onClick = { dispatch(ProfileOrdersIntent.CartClick) }
                    )

                    MessengerIconButton(
                        showBadge = state.showMessengerBadge,
                        onClick = { dispatch(ProfileOrdersIntent.MessengerClick) }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        SharedPullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { dispatch(ProfileOrdersIntent.PullToRefresh) },
            modifier = Modifier.fillMaxSize(),
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
            SharedLazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = innerPadding + PaddingValues(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(
                    when {
                        pagingItems.isRefreshLoading -> 8.dp
                        else -> 0.dp
                    }
                ),
                userScrollEnabled = !pagingItems.isRefreshLoading
            ) {
                if (pagingItems.isRefreshLoading) {
                    items(
                        count = 3
                    ) {
                        Spacer(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth()
                                .height(205.dp)
                                .placeholder(
                                    visible = pagingItems.isRefreshLoading,
                                    highlight = PlaceholderHighlight.shimmer(),
                                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                    shape = RoundedCornerShape(4.dp)
                                )
                        )
                    }
                } else {
                    items(
                        count = pagingItems.itemCount,
                        key = pagingItems.itemKey { order -> order.orderNumber },
                        contentType = pagingItems.itemContentType()
                    ) { index ->
                        val order = pagingItems[index]

                        if (order != null) {
                            ProfileOrderItem(
                                state = order,
                                onClick = {
                                    dispatch(
                                        ProfileOrdersIntent.OrderClick(
                                            orderNumber = order.orderNumber,
                                            amount = order.amount
                                        )
                                    )
                                },
                                onCopyClick = copyOrderNumber,
                                onMoreClick = {}
                            )

                            HorizontalDivider(
                                modifier = Modifier.padding(start = 16.dp),
                                color = MaterialTheme.colorScheme.divider
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
private fun ProfileOrdersScreenContentPreview() {
    val pagingItems = remember {
        MutableStateFlow(PagingData.from(profileOrdersPreviewItems()))
    }.collectAsLazyPagingItems()

    ProfileOrdersScreenContent(
        state = ProfileOrdersModel(),
        pagingItems = pagingItems,
        dispatch = {},
        copyOrderNumber = {}
    )
}
