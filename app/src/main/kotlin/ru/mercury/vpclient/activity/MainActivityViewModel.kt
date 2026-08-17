package ru.mercury.vpclient.activity

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainActivityEvent
import ru.mercury.vpclient.activity.intent.MainActivityIntent
import ru.mercury.vpclient.activity.model.MainActivityModel
import ru.mercury.vpclient.features.auth_welcome.navigation.WelcomeRoute
import ru.mercury.vpclient.features.main.navigation.MainRoute
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.usecase.PushNotificationsSheetVisibilityUseCase
import ru.mercury.vpclient.shared.domain.usecase.SavePushNotificationsSheetResultUseCase
import ru.mercury.vpclient.shared.domain.usecase.StartRealtimeUseCase
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
    private val pushNotificationsSheetVisibilityUseCase: PushNotificationsSheetVisibilityUseCase,
    private val savePushNotificationsSheetResultUseCase: SavePushNotificationsSheetResultUseCase,
    private val startRealtimeUseCase: StartRealtimeUseCase
): ClientViewModel<MainActivityIntent, MainActivityModel, MainActivityEvent>(MainActivityModel()) {

    init {
        dispatch(MainActivityIntent.ResolveNavigation)
    }

    override fun dispatch(intent: MainActivityIntent) {
        when (intent) {
            is MainActivityIntent.ResolveNavigation -> {
                launch {
                    val startDestination = if (settingsDataStore.getValue(PreferenceKey.UserToken).isNullOrEmpty()) WelcomeRoute else MainRoute()
                    val isPushNotificationsSheetVisible = startDestination is MainRoute &&
                        pushNotificationsSheetVisibilityUseCase(Unit).getOrThrow()
                    reduce {
                        it.copy(
                            splashLoading = false,
                            startDestination = startDestination,
                            isPushNotificationsSheetVisible = isPushNotificationsSheetVisible
                        )
                    }
                }
            }
            is MainActivityIntent.StartRealtime -> {
                if (stateFlow.value.realtimeJob?.isActive == true) return
                val job = launch { startRealtimeUseCase(Unit).getOrThrow() }.also { launchedJob ->
                    launchedJob.invokeOnCompletion { reduce { it.copy(realtimeJob = null) } }
                }
                reduce { it.copy(realtimeJob = job) }
            }
            is MainActivityIntent.StopRealtime -> {
                stateFlow.value.realtimeJob?.cancel()
                reduce { it.copy(realtimeJob = null) }
            }
            is MainActivityIntent.PushNotificationsSheetEnableClick -> {
                reduce { it.copy(isPushNotificationsSheetVisible = false) }
                launch {
                    savePushNotificationsSheetResultUseCase(false).getOrThrow()
                    send(MainActivityEvent.RequestPushNotificationsPermission)
                }
            }
            is MainActivityIntent.PushNotificationsSheetDismissClick -> {
                reduce { it.copy(isPushNotificationsSheetVisible = false) }
                launch { savePushNotificationsSheetResultUseCase(true).getOrThrow() }
            }
            is MainActivityIntent.CenterLoading -> reduce { it.copy(centerLoading = intent.enabled) }
        }
    }
}
