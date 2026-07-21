package ru.mercury.vpclient.features.notifications

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.cart.navigation.CartPage
import ru.mercury.vpclient.features.cart.navigation.CartRoute
import ru.mercury.vpclient.features.notifications.event.NotificationsEvent
import ru.mercury.vpclient.features.notifications.intent.NotificationsIntent
import ru.mercury.vpclient.features.notifications.model.NotificationsModel
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.type.ActivityCounterType
import ru.mercury.vpclient.shared.data.persistence.database.RoomException
import ru.mercury.vpclient.shared.data.persistence.database.RoomSQLiteException
import ru.mercury.vpclient.shared.domain.usecase.ActivityCountersByPairedUserIdResetUseCase
import ru.mercury.vpclient.shared.domain.usecase.ActivityCountersByPairedUserIdResetUseCase.ActivityCountersByPairedUserIdResetException
import ru.mercury.vpclient.shared.domain.usecase.ClientNotificationEntitiesFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.ClientNotificationsUseCase
import ru.mercury.vpclient.shared.domain.usecase.ClientNotificationsUseCase.ClientNotificationsException
import ru.mercury.vpclient.shared.domain.usecase.EmployeeActiveFlowUseCase
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.navigation.BackRoute
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val clientNotificationEntitiesFlowUseCase: ClientNotificationEntitiesFlowUseCase,
    private val clientNotificationsUseCase: ClientNotificationsUseCase,
    private val activityCountersByPairedUserIdResetUseCase: ActivityCountersByPairedUserIdResetUseCase,
    private val employeeActiveFlowUseCase: EmployeeActiveFlowUseCase
): ClientViewModel<NotificationsIntent, NotificationsModel, NotificationsEvent>(NotificationsModel()) {

    init {
        dispatch(NotificationsIntent.CollectNotifications)
        dispatch(NotificationsIntent.CollectActiveEmployee)
        dispatch(NotificationsIntent.LoadNotifications)
        dispatch(NotificationsIntent.ResetNotificationCounter)
    }

    override fun dispatch(intent: NotificationsIntent) {
        when (intent) {
            is NotificationsIntent.CollectNotifications -> {
                val category = stateFlow.value.selectedCategory
                val job = launch {
                    clientNotificationEntitiesFlowUseCase(category).collectLatest { entities ->
                        reduce { state -> state.copy(notificationEntities = entities) }
                    }
                }
                reduce { state -> state.copy(collectNotificationsJob = job) }
            }
            is NotificationsIntent.CollectActiveEmployee -> {
                launch {
                    employeeActiveFlowUseCase(Unit).collectLatest { entity ->
                        reduce { state -> state.copy(activeEmployee = entity) }
                    }
                }
            }
            is NotificationsIntent.LoadNotifications -> {
                val state = stateFlow.value
                if (state.clientNotificationsJob?.isActive == true || state.refreshNotificationsJob?.isActive == true) {
                    return
                }
                val job = launch {
                    clientNotificationsUseCase(state.selectedCategory).getOrThrow()
                }.also { launchedJob ->
                    launchedJob.invokeOnCompletion {
                        reduce { currentState ->
                            when {
                                currentState.clientNotificationsJob === launchedJob -> {
                                    currentState.copy(clientNotificationsJob = null)
                                }
                                else -> currentState
                            }
                        }
                    }
                }
                reduce { currentState -> currentState.copy(clientNotificationsJob = job) }
            }
            is NotificationsIntent.ResetNotificationCounter -> {
                launch {
                    activityCountersByPairedUserIdResetUseCase(ActivityCounterType.CLIENT_NOTIFICATION).getOrThrow()
                }
            }
            is NotificationsIntent.PullToRefresh -> {
                val state = stateFlow.value
                if (state.clientNotificationsJob?.isActive == true || state.refreshNotificationsJob?.isActive == true) {
                    return
                }
                val job = launch {
                    clientNotificationsUseCase(state.selectedCategory).getOrThrow()
                }.also { launchedJob ->
                    launchedJob.invokeOnCompletion {
                        reduce { currentState ->
                            when {
                                currentState.refreshNotificationsJob === launchedJob -> {
                                    currentState.copy(refreshNotificationsJob = null)
                                }
                                else -> currentState
                            }
                        }
                    }
                }
                reduce { currentState -> currentState.copy(refreshNotificationsJob = job) }
            }
            is NotificationsIntent.BackClick -> launch { MainEventManager.send(BackRoute) }
            is NotificationsIntent.CartClick -> launch { MainEventManager.send(CartRoute()) }
            is NotificationsIntent.FittingClick -> {
                launch { MainEventManager.send(CartRoute(CartPage.Fitting)) }
            }
            is NotificationsIntent.MessengerClick -> return
            is NotificationsIntent.SelectCategory -> {
                val state = stateFlow.value
                if (state.selectedCategory == intent.category) return
                state.collectNotificationsJob?.cancel()
                state.clientNotificationsJob?.cancel()
                state.refreshNotificationsJob?.cancel()
                reduce { currentState ->
                    currentState.copy(
                        notificationEntities = emptyList(),
                        selectedCategory = intent.category,
                        collectNotificationsJob = null,
                        clientNotificationsJob = null,
                        refreshNotificationsJob = null
                    )
                }
                dispatch(NotificationsIntent.CollectNotifications)
                dispatch(NotificationsIntent.LoadNotifications)
            }
            is NotificationsIntent.NotificationClick -> {
                if (intent.deepLinkUrl.isNotBlank()) {
                    launch { send(NotificationsEvent.OpenDeepLink(intent.deepLinkUrl)) }
                }
            }
        }
    }

    override fun catch(throwable: Throwable) {
        when (throwable) {
            is ClientNotificationsException -> {
                reduce { state ->
                    state.copy(
                        clientNotificationsJob = null,
                        refreshNotificationsJob = null
                    )
                }
                launch { send(NotificationsEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is ActivityCountersByPairedUserIdResetException -> {
                launch { send(NotificationsEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is ClientException -> {
                reduce { state ->
                    state.copy(
                        clientNotificationsJob = null,
                        refreshNotificationsJob = null
                    )
                }
                launch { send(NotificationsEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is RoomException, is RoomSQLiteException -> {
                launch { send(NotificationsEvent.SnackbarErrorMessage(throwable.message.orEmpty())) }
            }
            else -> super.catch(throwable)
        }
    }
}
