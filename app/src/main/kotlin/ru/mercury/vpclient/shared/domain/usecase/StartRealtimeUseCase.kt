package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.mercury.vpclient.BuildConfig
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.APP_FULL_VERSION
import ru.mercury.vpclient.shared.data.APP_VERSION
import ru.mercury.vpclient.shared.data.DEFAULT_EMPLOYEE_APP
import ru.mercury.vpclient.shared.data.network.env.ClientEnvironment
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.data.realtime.ActivityRealtimeDataSource
import ru.mercury.vpclient.shared.data.realtime.BasketRealtimeDataSource
import ru.mercury.vpclient.shared.data.realtime.ClientRealtimeDataSource
import ru.mercury.vpclient.shared.data.realtime.FittingRealtimeDataSource
import ru.mercury.vpclient.shared.data.realtime.RealtimeSession
import timber.log.Timber
import javax.inject.Inject

class StartRealtimeUseCase @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
    private val activityRealtimeDataSource: ActivityRealtimeDataSource,
    private val basketRealtimeDataSource: BasketRealtimeDataSource,
    private val fittingRealtimeDataSource: FittingRealtimeDataSource,
    private val clientRealtimeDataSource: ClientRealtimeDataSource,
    private val activityCountersUseCase: ActivityCountersUseCase,
    private val loadBasketUseCase: LoadBasketUseCase,
    private val loadFittingUseCase: LoadFittingUseCase,
    private val myEmployeesUseCase: MyEmployeesUseCase,
    private val myEmployeeUseCase: MyEmployeeUseCase,
    private val myEmployeeBadgesUseCase: MyEmployeeBadgesUseCase,
    dispatchers: SharedDispatchers
): UseCase<Unit, Unit>(dispatchers.io) {

    override suspend fun execute(params: Unit) {
        combine(
            settingsDataStore.getValueFlow(PreferenceKey.UserToken),
            settingsDataStore.getValueFlow(PreferenceKey.UserId),
            settingsDataStore.getValueFlow(PreferenceKey.PairedUser),
            settingsDataStore.getValueFlow(PreferenceKey.ApplicationType),
            settingsDataStore.getValueFlow(environmentPreferenceKey())
        ) { token, userId, pairedUserId, applicationType, environment ->
            val currentToken = token.orEmpty()
            val currentUserId = userId.orEmpty()
            if (currentToken.isEmpty() || currentUserId.isEmpty()) return@combine null

            val headers = buildMap {
                put("X-ApplicationType", applicationType.orEmpty().ifEmpty { DEFAULT_APPLICATION_TYPE })
                put("X-AppVersion", APP_VERSION)
                put("X-AppFullVersion", APP_FULL_VERSION)
                put("X-EmployeeApp", DEFAULT_EMPLOYEE_APP)
                put("X-AppBuild", BuildConfig.VERSION_CODE.toString())
                put("X-User", currentUserId)
                put("X-UserToken", currentToken)
                put("X-Token", currentToken)
                pairedUserId.orEmpty().takeIf(String::isNotEmpty)?.let { pairedUser ->
                    put("X-PairedUser", pairedUser)
                }
            }
            RealtimeSession(
                baseUrl = "${resolveEnvironment(environment).url}$SOCKET_PATH",
                token = currentToken,
                userId = currentUserId,
                pairedUserId = pairedUserId.orEmpty(),
                headers = headers
            )
        }.distinctUntilChanged().collectLatest { session ->
            if (session == null) return@collectLatest

            coroutineScope {
                launch(start = CoroutineStart.UNDISPATCHED) {
                    clientRealtimeDataSource.allUpdates.conflate().collect {
                        runCatching { myEmployeesUseCase(Unit).getOrThrow() }.onFailure { throwable ->
                            if (throwable is CancellationException) throw throwable
                            Timber.w(throwable, "Failed to refresh employees after realtime update")
                        }
                    }
                }
                launch(start = CoroutineStart.UNDISPATCHED) {
                    clientRealtimeDataSource.employeeUpdates.collect { employeeId ->
                        if (employeeId.isNotEmpty()) {
                            runCatching { myEmployeeUseCase(employeeId).getOrThrow() }.onFailure { throwable ->
                                if (throwable is CancellationException) throw throwable
                                Timber.w(throwable, "Failed to refresh employee after realtime update")
                            }
                            runCatching { myEmployeeBadgesUseCase(Unit).getOrThrow() }.onFailure { throwable ->
                                if (throwable is CancellationException) throw throwable
                                Timber.w(throwable, "Failed to refresh employee badges after realtime update")
                            }
                            runCatching { activityCountersUseCase(Unit).getOrThrow() }.onFailure { throwable ->
                                if (throwable is CancellationException) throw throwable
                                Timber.w(throwable, "Failed to refresh activity counters after realtime update")
                            }
                        }
                    }
                }
                launch { clientRealtimeDataSource.connect(session) }

                if (session.pairedUserId.isNotEmpty()) {
                    launch(start = CoroutineStart.UNDISPATCHED) {
                        activityRealtimeDataSource.updates.conflate().collect {
                            runCatching { activityCountersUseCase(Unit).getOrThrow() }.onFailure { throwable ->
                                if (throwable is CancellationException) throw throwable
                                Timber.w(throwable, "Failed to refresh activity counters after realtime update")
                            }
                        }
                    }
                    launch(start = CoroutineStart.UNDISPATCHED) {
                        basketRealtimeDataSource.updates.conflate().collect {
                            runCatching { loadBasketUseCase(Unit).getOrThrow() }.onFailure { throwable ->
                                if (throwable is CancellationException) throw throwable
                                Timber.w(throwable, "Failed to refresh basket after realtime update")
                            }
                        }
                    }
                    launch(start = CoroutineStart.UNDISPATCHED) {
                        fittingRealtimeDataSource.updates.conflate().collect {
                            runCatching { loadFittingUseCase(Unit).getOrThrow() }.onFailure { throwable ->
                                if (throwable is CancellationException) throw throwable
                                Timber.w(throwable, "Failed to refresh fitting after realtime update")
                            }
                        }
                    }
                    launch { activityRealtimeDataSource.connect(session) }
                    launch { basketRealtimeDataSource.connect(session) }
                    launch { fittingRealtimeDataSource.connect(session) }
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
        if (BuildConfig.FLAVOR == "prod") return ClientEnvironment.PROD
        val normalized = value?.trim().orEmpty()
        val storedEnvironment = ClientEnvironment.entries.firstOrNull { environment ->
            environment.name.equals(normalized, ignoreCase = true) || environment.url.equals(normalized, ignoreCase = true)
        } ?: when (normalized.lowercase()) {
            "dev", "test" -> ClientEnvironment.TEST
            "uat" -> ClientEnvironment.UAT
            "prod", "production" -> ClientEnvironment.PROD
            else -> null
        }
        if (storedEnvironment != null) return storedEnvironment
        return when (BuildConfig.VPCLIENT_ENV.lowercase()) {
            "uat" -> ClientEnvironment.UAT
            "prod", "production" -> ClientEnvironment.PROD
            else -> ClientEnvironment.TEST
        }
    }

    private companion object {
        private const val SOCKET_PATH = "ws/v1_63/"
        private const val DEFAULT_APPLICATION_TYPE = "api"
    }
}
