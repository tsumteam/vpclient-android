@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import javax.inject.Inject
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore

class SavePushNotificationsSheetResultUseCase @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): UseCase<Boolean, Unit>(dispatchers.io) {

    override suspend fun execute(wasRejected: Boolean) {
        settingsDataStore.setValue(
            PreferenceKey.PushNotificationsPromptTimestamp,
            System.currentTimeMillis()
        )
        settingsDataStore.setValue(
            PreferenceKey.PushNotificationsPromptRejected,
            wasRejected
        )
    }
}
