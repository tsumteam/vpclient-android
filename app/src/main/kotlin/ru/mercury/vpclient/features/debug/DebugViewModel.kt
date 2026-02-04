package ru.mercury.vpclient.features.debug

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import ru.mercury.vpclient.BuildConfig
import ru.mercury.vpclient.core.event.SnackbarEvent
import ru.mercury.vpclient.core.mvi.VPClientViewModel
import ru.mercury.vpclient.core.navigation.BackRoute
import ru.mercury.vpclient.core.network.env.VPClientEnvironment
import ru.mercury.vpclient.core.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.core.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.features.debug.intent.DebugIntent
import ru.mercury.vpclient.features.debug.model.DebugModel
import ru.mercury.vpclient.main.event.MainEventManager
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
class DebugViewModel @Inject constructor(
    private val settingsDataStore: Provider<SettingsDataStore>
): VPClientViewModel<DebugIntent, DebugModel>(DebugModel()) {

    init {
        dispatch(DebugIntent.FetchSettings)
    }

    override fun dispatch(intent: DebugIntent) {
        when (intent) {
            is DebugIntent.BackClick -> launch { push(BackRoute) }
            is DebugIntent.FetchSettings -> {
                launch {
                    combine(
                        settingsDataStore.get().getValueFlow(PreferenceKey.DeviceId),
                        settingsDataStore.get().getValueFlow(PreferenceKey.UserToken),
                        settingsDataStore.get().getValueFlow(environmentPreferenceKey())
                    ) { deviceId, userToken, environment ->
                        DebugModel(
                            deviceId = deviceId.orEmpty(),
                            userToken = userToken.orEmpty(),
                            environment = VPClientEnvironment.valueOf(environment ?: BuildConfig.VPCLIENT_ENV)
                        )
                    }.collectLatest { model -> reduce { model } }
                }
            }
            is DebugIntent.EnvironmentClick -> reduce { it.copy(environmentDialog = true) }
            is DebugIntent.DismissEnvironmentDialog -> reduce { it.copy(environmentDialog = false) }
            is DebugIntent.SelectEnvironment -> {
                launch {
                    settingsDataStore.get().setValue(environmentPreferenceKey(), intent.environment.name)
                    MainEventManager.send(SnackbarEvent("Окружение изменено на ${intent.environment.name}"))
                }
            }
            is DebugIntent.DropLocalDbClick -> {
                launch {
                    MainEventManager.send(SnackbarEvent("База данных очищена"))
                }
            }
        }
    }

    private fun environmentPreferenceKey(): PreferenceKey<String> {
        return when (BuildConfig.FLAVOR) {
            "prod" -> PreferenceKey.EnvironmentProd
            "uat" -> PreferenceKey.EnvironmentUat
            "dev" -> PreferenceKey.EnvironmentDev
            else -> PreferenceKey.EnvironmentDev
        }
    }
}
