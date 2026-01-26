package ru.mercury.vpclient.features.debug

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import ru.mercury.vpclient.core.event.SnackbarEvent
import ru.mercury.vpclient.core.interactor.Interactor
import ru.mercury.vpclient.core.ktx.orEmpty
import ru.mercury.vpclient.core.mvi.VPClientViewModel
import ru.mercury.vpclient.core.navigation.BackRoute
import ru.mercury.vpclient.core.network.NetworkService
import ru.mercury.vpclient.core.network.env.VPClientEnvironment
import ru.mercury.vpclient.core.persistence.database.AppDatabase
import ru.mercury.vpclient.core.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.core.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.features.debug.intent.DebugIntent
import ru.mercury.vpclient.features.debug.model.DebugModel
import ru.mercury.vpclient.main.event.MainEventManager
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
class DebugViewModel @Inject constructor(
    private val settingsDataStore: Provider<SettingsDataStore>,
    private val appDatabase: Provider<AppDatabase>,
    private val networkService: Provider<NetworkService>,
    private val interactor: Interactor
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
                        settingsDataStore.get().getValueFlow(PreferenceKey.Autofill),
                        settingsDataStore.get().getValueFlow(PreferenceKey.RequestDelay),
                        settingsDataStore.get().getValueFlow(PreferenceKey.Environment)
                    ) { deviceId, userToken, autofill, requestDelayMs, environment ->
                        DebugModel(
                            deviceId = deviceId.orEmpty(),
                            userToken = userToken.orEmpty(),
                            autofill = autofill.orEmpty,
                            requestDelayMs = requestDelayMs.orEmpty,
                            environment = VPClientEnvironment.valueOf(environment ?: VPClientEnvironment.BCA.name)
                        )
                    }.collectLatest { model -> reduce { model } }
                }
            }
            is DebugIntent.EnvironmentClick -> reduce { it.copy(environmentDialog = true) }
            is DebugIntent.DismissEnvironmentDialog -> reduce { it.copy(environmentDialog = false) }
            is DebugIntent.SelectEnvironment -> {
                launch {
                    settingsDataStore.get().setValue(PreferenceKey.Environment, intent.environment.name)
                    MainEventManager.send(SnackbarEvent("Окружение изменено на ${intent.environment.name}"))
                }
            }
            is DebugIntent.DropLocalDbClick -> {
                launch {
                    MainEventManager.send(SnackbarEvent("База данных очищена"))
                }
            }
            is DebugIntent.AutofillClick -> {
                launch { settingsDataStore.get().setValue(PreferenceKey.Autofill, !stateFlow.value.autofill) }
            }
            is DebugIntent.RequestDelayChange -> reduce { it.copy(requestDelayMs = intent.value) }
            is DebugIntent.RequestDelayChangeFinished -> {
                launch { settingsDataStore.get().setValue(PreferenceKey.RequestDelay, stateFlow.value.requestDelayMs) }
            }
            is DebugIntent.NetworkRequest -> {}
            is DebugIntent.DatabaseRequest -> {
                launch(Dispatchers.IO) {
                    //val deliveryEntity = appDatabase.get().deliveryDao().select("ЗД02777626")
                    //appDatabase.get().deliveryDao().delete(deliveryEntity)

                    //val paymentPojo = appDatabase.get().deliveryDao().selectPaymentPojo("ЗД02777626")
                    //appDatabase.get().productDao().update(paymentPojo.paymentProductEntities.map { it.copy(status = ProductResponse.STATUS_SOLD, isToPay = false) })

                    /*val deliveryEntity1 = appDatabase.get().deliveryDao().select("ЗД02777524")
                    appDatabase.get().deliveryDao().update(deliveryEntity1.copy(parentDeliveryId = "ЗД02777513"))

                    val deliveryEntity2 = appDatabase.get().deliveryDao().select("ЗД02777525")
                    appDatabase.get().deliveryDao().update(deliveryEntity2.copy(parentDeliveryId = "ЗД02777513"))*/

                    val deliveryEntity = appDatabase.get().deliveryDao().select("ЗД02777631")
                    appDatabase.get().deliveryDao().update(deliveryEntity.copy(orderId = ""))

                    MainEventManager.send(SnackbarEvent("Успешно"))
                }
            }
        }
    }
}
