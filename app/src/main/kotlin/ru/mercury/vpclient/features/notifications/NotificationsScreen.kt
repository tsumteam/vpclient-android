@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.notifications

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.notifications.event.NotificationsEvent
import ru.mercury.vpclient.features.notifications.intent.NotificationsIntent
import ru.mercury.vpclient.features.notifications.model.NotificationsModel
import ru.mercury.vpclient.shared.data.entity.ClientNotificationCategory
import ru.mercury.vpclient.shared.data.persistence.database.entity.ClientNotificationEntity
import ru.mercury.vpclient.shared.domain.mapper.clientNotificationDateText
import ru.mercury.vpclient.shared.ui.components.EmptyBox
import ru.mercury.vpclient.shared.ui.components.EmptyBoxState
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedPullToRefreshBox
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.SharedSnackbarHost
import ru.mercury.vpclient.shared.ui.components.cart.CartIconButton
import ru.mercury.vpclient.shared.ui.components.cart.FittingIconButton
import ru.mercury.vpclient.shared.ui.components.cart.MessengerIconButton
import ru.mercury.vpclient.shared.ui.components.notification.NotificationFilterChip
import ru.mercury.vpclient.shared.ui.components.notification.NotificationFilterChipState
import ru.mercury.vpclient.shared.ui.components.notification.NotificationItem
import ru.mercury.vpclient.shared.ui.components.notification.NotificationItemState
import ru.mercury.vpclient.shared.ui.icons.ChevronStart24
import ru.mercury.vpclient.shared.ui.icons.VipPlatinumEmpty
import ru.mercury.vpclient.shared.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.shared.ui.placeholder
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium18

@Composable
fun NotificationsScreen(
    viewModel: NotificationsViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostStateError = remember { SnackbarHostState() }

    NotificationsScreenContent(
        state = state,
        dispatch = viewModel::dispatch,
        snackbarHostStateError = snackbarHostStateError
    )

    ObserveAsEvents(
        flow = viewModel.eventFlow
    ) { event ->
        when (event) {
            is NotificationsEvent.OpenDeepLink -> {
                context.startActivity(Intent(Intent.ACTION_VIEW, event.deepLinkUrl.toUri()))
            }
            is NotificationsEvent.SnackbarErrorMessage -> {
                snackbarHostStateError.currentSnackbarData?.dismiss()
                scope.launch { snackbarHostStateError.showSnackbar(event.message) }
            }
        }
    }
}

@Composable
private fun NotificationsScreenContent(
    state: NotificationsModel,
    dispatch: (NotificationsIntent) -> Unit,
    snackbarHostStateError: SnackbarHostState
) {
    val pullToRefreshState = rememberPullToRefreshState()

    SharedScaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(ClientStrings.NotificationsTitle),
                            style = MaterialTheme.typography.medium18
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { dispatch(NotificationsIntent.BackClick) },
                            modifier = Modifier.size(42.dp)
                        ) {
                            Icon(
                                imageVector = ChevronStart24,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    actions = {
                        FittingIconButton(
                            text = state.fittingText,
                            showBadge = state.isFittingBadgeVisible,
                            onClick = { dispatch(NotificationsIntent.FittingClick) }
                        )

                        CartIconButton(
                            text = state.cartText,
                            showBadge = state.isCartBadgeVisible,
                            onClick = { dispatch(NotificationsIntent.CartClick) }
                        )

                        MessengerIconButton(
                            showBadge = state.isMessengerBadgeVisible,
                            onClick = { dispatch(NotificationsIntent.MessengerClick) }
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                        titleContentColor = MaterialTheme.colorScheme.onBackground,
                        actionIconContentColor = MaterialTheme.colorScheme.onBackground
                    )
                )

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        NotificationFilterChip(
                            state = NotificationFilterChipState(
                                text = stringResource(ClientStrings.NotificationsAllCaps),
                                isSelected = state.selectedCategory == ClientNotificationCategory.ALL,
                                isEnabled = true,
                                onClick = { dispatch(NotificationsIntent.SelectCategory(ClientNotificationCategory.ALL)) }
                            )
                        )
                    }
                    item {
                        NotificationFilterChip(
                            state = NotificationFilterChipState(
                                text = stringResource(ClientStrings.NotificationsConsultantsCaps),
                                isSelected = state.selectedCategory == ClientNotificationCategory.CONSULTANTS,
                                isEnabled = true,
                                onClick = { dispatch(NotificationsIntent.SelectCategory(ClientNotificationCategory.CONSULTANTS)) }
                            )
                        )
                    }
                    item {
                        NotificationFilterChip(
                            state = NotificationFilterChipState(
                                text = stringResource(ClientStrings.NotificationsCatalogsAndActionsCaps),
                                isSelected = state.selectedCategory == ClientNotificationCategory.CATALOGS_AND_ACTIONS,
                                isEnabled = true,
                                onClick = { dispatch(NotificationsIntent.SelectCategory(ClientNotificationCategory.CATALOGS_AND_ACTIONS)) }
                            )
                        )
                    }
                }
            }
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
            onRefresh = { dispatch(NotificationsIntent.PullToRefresh) },
            modifier = Modifier.fillMaxSize(),
            enabled = state.isPullToRefreshEnabled,
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
                contentPadding = innerPadding,
                userScrollEnabled = state.isContentVisible
            ) {
                if (state.isLoading) {
                    items(
                        count = 5
                    ) {
                        Column {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(90.dp)
                                    .padding(horizontal = 16.dp)
                                    .placeholder(shape = RoundedCornerShape(4.dp))
                            )

                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(2.dp)
                            )
                        }
                    }
                }
                if (state.isContentVisible) {
                    items(
                        items = state.notificationEntities,
                        key = { entity -> entity.id }
                    ) { entity ->
                        NotificationItem(
                            state = NotificationItemState(
                                notificationEntity = entity,
                                dateText = entity.timestamp.clientNotificationDateText(stringResource(ClientStrings.NotificationsYesterday)),
                                onClick = { dispatch(NotificationsIntent.NotificationClick(entity.deepLinkUrl)) }
                            )
                        )
                    }
                }
                if (state.isEmptyVisible) {
                    item {
                        EmptyBox(
                            state = EmptyBoxState(
                                imageVector = VipPlatinumEmpty,
                                text = stringResource(
                                    when (state.selectedCategory) {
                                        ClientNotificationCategory.ALL -> ClientStrings.NotificationsEmpty
                                        else -> ClientStrings.NotificationsFilterEmpty
                                    }
                                )
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(560.dp)
                                .padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun NotificationsScreenContentPreview(
    @PreviewParameter(NotificationsModelPreviewParameterProvider::class) state: NotificationsModel
) {
    NotificationsScreenContent(
        state = state,
        dispatch = {},
        snackbarHostStateError = remember { SnackbarHostState() }
    )
}

private class NotificationsModelPreviewParameterProvider: PreviewParameterProvider<NotificationsModel> {
    override val values: Sequence<NotificationsModel> = sequenceOf(
        NotificationsModel(
            clientNotificationsJob = Job()
        ),
        NotificationsModel(
            notificationEntities = listOf(
                ClientNotificationEntity(
                    id = 1,
                    category = ClientNotificationCategory.ALL,
                    badge = 1,
                    type = 1,
                    imageUrl = "",
                    title = "Новое сообщение",
                    message = "Консультант отправил Вам новую подборку",
                    deepLinkUrl = "vipplatinum://compilation/1",
                    timestamp = "2026-07-21T10:30:00+03:00"
                ),
                ClientNotificationEntity(
                    id = 2,
                    category = ClientNotificationCategory.ALL,
                    badge = 0,
                    type = 2,
                    imageUrl = "",
                    title = "Акция: «3 в 1»",
                    message = "Новая акция уже доступна в приложении",
                    deepLinkUrl = "",
                    timestamp = "2026-07-20T10:30:00+03:00"
                )
            )
        ),
        NotificationsModel(
            selectedCategory = ClientNotificationCategory.CONSULTANTS
        )
    )
}
