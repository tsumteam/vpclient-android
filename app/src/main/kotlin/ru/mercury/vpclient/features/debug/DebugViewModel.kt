package ru.mercury.vpclient.features.debug

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.mercury.vpclient.BuildConfig
import ru.mercury.vpclient.shared.domain.mapper.orEmpty
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import ru.mercury.vpclient.shared.data.network.env.ClientEnvironment
import ru.mercury.vpclient.shared.data.persistence.database.AppDatabase
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.features.debug.event.DebugEvent
import ru.mercury.vpclient.features.debug.intent.DebugIntent
import ru.mercury.vpclient.features.debug.model.DebugModel
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
class DebugViewModel @Inject constructor(
    private val appDatabase: Provider<AppDatabase>,
    private val settingsDataStore: Provider<SettingsDataStore>
): ClientViewModel<DebugIntent, DebugModel, Event>(DebugModel()) {

    init {
        dispatch(DebugIntent.FetchSettings)
    }

    override fun dispatch(intent: DebugIntent) {
        when (intent) {
            is DebugIntent.BackClick -> launch { send(DebugEvent.FinishScreen) }
            is DebugIntent.FetchSettings -> {
                launch {
                    combine(
                        settingsDataStore.get().getValueFlow(PreferenceKey.DeviceId),
                        settingsDataStore.get().getValueFlow(PreferenceKey.UserToken),
                        settingsDataStore.get().getValueFlow(environmentPreferenceKey()),
                        settingsDataStore.get().getValueFlow(PreferenceKey.RequestDelay)
                    ) { deviceId, userToken, environment, requestDelay ->
                        DebugModel(
                            deviceId = deviceId.orEmpty(),
                            userToken = userToken.orEmpty(),
                            environment = resolveEnvironment(environment),
                            requestDelayEnabled = requestDelay.orEmpty > 0F
                        )
                    }.collectLatest { model -> reduce { model } }
                }
            }
            is DebugIntent.EnvironmentClick -> reduce { it.copy(environmentDialog = true) }
            is DebugIntent.DismissEnvironmentDialog -> reduce { it.copy(environmentDialog = false) }
            is DebugIntent.ToggleRequestDelay -> launch { settingsDataStore.get().setValue(PreferenceKey.RequestDelay, if (intent.enabled) 5_000L else 0L) }
            is DebugIntent.SelectEnvironment -> {
                launch {
                    settingsDataStore.get().setValue(environmentPreferenceKey(), intent.environment.name)
                    send(DebugEvent.SnackbarMessage("Окружение изменено на ${intent.environment.name}"))
                }
            }
            is DebugIntent.DropLocalDbClick -> {
                launch {
                    withContext(Dispatchers.IO) { appDatabase.get().clearAllTables() }
                    send(DebugEvent.SnackbarMessage("База данных очищена"))
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

    private fun resolveEnvironment(value: String?): ClientEnvironment {
        return parseEnvironment(value) ?: parseEnvironment(BuildConfig.VPCLIENT_ENV) ?: ClientEnvironment.TEST
    }

    private fun parseEnvironment(value: String?): ClientEnvironment? {
        val normalized = value?.trim().orEmpty()
        if (normalized.isEmpty()) return null
        return ClientEnvironment.entries.firstOrNull {
            it.name.equals(normalized, ignoreCase = true) || it.url.equals(normalized, ignoreCase = true)
        } ?: when (normalized.lowercase()) {
            "dev", "test" -> ClientEnvironment.TEST
            "uat" -> ClientEnvironment.UAT
            "prod", "production" -> ClientEnvironment.PROD
            else -> null
        }
    }
}
