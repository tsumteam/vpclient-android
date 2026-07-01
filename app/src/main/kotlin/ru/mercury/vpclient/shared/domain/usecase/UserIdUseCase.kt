package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import javax.inject.Inject

// fixme
class UserIdUseCase @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): UseCase<Unit, String>(dispatchers.io) {

    override suspend fun execute(params: Unit): String {
        return settingsDataStore.getValue(PreferenceKey.UserId).orEmpty()
    }
}
