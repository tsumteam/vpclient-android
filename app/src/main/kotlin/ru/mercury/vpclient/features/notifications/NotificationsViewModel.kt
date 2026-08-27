package ru.mercury.vpclient.features.notifications

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.cart.navigation.CartPage
import ru.mercury.vpclient.features.cart.navigation.CartRoute
import ru.mercury.vpclient.features.compilation.navigation.CompilationRoute
import ru.mercury.vpclient.features.notifications.event.NotificationsEvent
import ru.mercury.vpclient.features.notifications.intent.NotificationsIntent
import ru.mercury.vpclient.features.notifications.model.NotificationsModel
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.type.ActivityCounterType
import ru.mercury.vpclient.shared.data.persistence.database.RoomException
import ru.mercury.vpclient.shared.data.persistence.database.RoomSQLiteException
import ru.mercury.vpclient.shared.domain.mapper.clientNotificationCompilationId
import ru.mercury.vpclient.shared.domain.usecase.ActivityCountersByPairedUserIdResetUseCase
import ru.mercury.vpclient.shared.domain.usecase.ActivityCountersByPairedUserIdResetUseCase.ActivityCountersByPairedUserIdResetException
import ru.mercury.vpclient.shared.domain.usecase.CartCountFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.ClientNotificationEntitiesFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.ClientNotificationsUseCase
import ru.mercury.vpclient.shared.domain.usecase.ClientNotificationsUseCase.ClientNotificationsException
import ru.mercury.vpclient.shared.domain.usecase.CompilationEntitiesFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.CompilationsClientUseCase
import ru.mercury.vpclient.shared.domain.usecase.CompilationsClientUseCase.CompilationsClientException
import ru.mercury.vpclient.shared.domain.usecase.EmployeeActiveFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.FittingCountFlowUseCase
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.navigation.BackRoute
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val clientNotificationEntitiesFlowUseCase: ClientNotificationEntitiesFlowUseCase,
    private val clientNotificationsUseCase: ClientNotificationsUseCase,
    private val compilationEntitiesFlowUseCase: CompilationEntitiesFlowUseCase,
    private val compilationsClientUseCase: CompilationsClientUseCase,
    private val activityCountersByPairedUserIdResetUseCase: ActivityCountersByPairedUserIdResetUseCase,
    private val cartCountFlowUseCase: CartCountFlowUseCase,
    private val fittingCountFlowUseCase: FittingCountFlowUseCase,
    private val employeeActiveFlowUseCase: EmployeeActiveFlowUseCase
): ClientViewModel<NotificationsIntent, NotificationsModel, NotificationsEvent>(NotificationsModel()) {

    init {
        dispatch(NotificationsIntent.CollectNotifications)
        dispatch(NotificationsIntent.CollectCompilations)
        dispatch(NotificationsIntent.CollectCartCount)
        dispatch(NotificationsIntent.CollectFittingCount)
        dispatch(NotificationsIntent.CollectActiveEmployee)
        dispatch(NotificationsIntent.LoadNotifications)
        dispatch(NotificationsIntent.LoadCompilations)
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
            is NotificationsIntent.CollectCompilations -> {
                launch {
                    compilationEntitiesFlowUseCase(Unit).collectLatest { entities ->
                        reduce { state -> state.copy(compilationEntities = entities) }
                    }
                }
            }
            is NotificationsIntent.CollectCartCount -> {
                launch {
                    cartCountFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { count -> reduce { it.copy(cartCount = count) } }
                }
            }
            is NotificationsIntent.CollectFittingCount -> {
                launch {
                    fittingCountFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { count -> reduce { it.copy(fittingCount = count) } }
                }
            }
            is NotificationsIntent.CollectActiveEmployee -> {
                launch {
                    employeeActiveFlowUseCase(Unit).collectLatest { entity ->
                        reduce { state -> state.copy(activeEmployee = entity) }
                    }
                }
            }
            is NotificationsIntent.LoadNotifications -> {
                if (stateFlow.value.clientNotificationsJob?.isActive == true || stateFlow.value.refreshNotificationsJob?.isActive == true) return
                val job = launch {
                    clientNotificationsUseCase(stateFlow.value.selectedCategory).getOrThrow()
                }.also { launchedJob ->
                    launchedJob.invokeOnCompletion {
                        reduce { it.copy(clientNotificationsJob = null) }
                    }
                }
                reduce { currentState -> currentState.copy(clientNotificationsJob = job) }
            }
            is NotificationsIntent.LoadCompilations -> {
                launch { compilationsClientUseCase(Unit).getOrThrow() }
            }
            is NotificationsIntent.ResetNotificationCounter -> {
                launch { activityCountersByPairedUserIdResetUseCase(ActivityCounterType.CLIENT_NOTIFICATION).getOrThrow() }
            }
            is NotificationsIntent.PullToRefresh -> {
                if (stateFlow.value.clientNotificationsJob?.isActive == true || stateFlow.value.refreshNotificationsJob?.isActive == true) return
                dispatch(NotificationsIntent.LoadCompilations)
                val job = launch {
                    clientNotificationsUseCase(stateFlow.value.selectedCategory).getOrThrow()
                }.also { launchedJob ->
                    launchedJob.invokeOnCompletion {
                        reduce { it.copy(refreshNotificationsJob = null) }
                    }
                }
                reduce { currentState -> currentState.copy(refreshNotificationsJob = job) }
            }
            is NotificationsIntent.BackClick -> launch { MainEventManager.send(BackRoute) }
            is NotificationsIntent.CartClick -> launch { MainEventManager.send(CartRoute()) }
            is NotificationsIntent.FittingClick -> {
                launch { MainEventManager.send(CartRoute(CartPage.Fitting)) }
            }
            is NotificationsIntent.MessengerClick -> {
                launch { MainEventManager.send(CartRoute(navigateToMessenger = true)) }
            }
            is NotificationsIntent.SelectCategory -> {
                if (stateFlow.value.selectedCategory == intent.category) return
                stateFlow.value.collectNotificationsJob?.cancel()
                stateFlow.value.clientNotificationsJob?.cancel()
                stateFlow.value.refreshNotificationsJob?.cancel()
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
                val compilationId = intent.deepLinkUrl.clientNotificationCompilationId
                when {
                    compilationId != null -> {
                        launch { MainEventManager.send(CompilationRoute(id = compilationId)) }
                    }
                    intent.deepLinkUrl.isNotBlank() -> {
                        launch { send(NotificationsEvent.OpenDeepLink(intent.deepLinkUrl)) }
                    }
                    else -> return
                }
            }
        }
    }

    override fun catch(throwable: Throwable) {
        when (throwable) {
            is ClientNotificationsException -> {
                reduce { it.copy(clientNotificationsJob = null, refreshNotificationsJob = null) }
                launch { send(NotificationsEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is ActivityCountersByPairedUserIdResetException -> {
                launch { send(NotificationsEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is CompilationsClientException -> {
                launch { send(NotificationsEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is ClientException -> {
                reduce { it.copy(clientNotificationsJob = null, refreshNotificationsJob = null) }
                launch { send(NotificationsEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is RoomException, is RoomSQLiteException -> {
                launch { send(NotificationsEvent.SnackbarErrorMessage(throwable.message.orEmpty())) }
            }
            else -> super.catch(throwable)
        }
    }
}
