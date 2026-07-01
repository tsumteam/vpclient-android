package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.persistence.database.dao.ClientDao
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import javax.inject.Inject

// fixme
class LogoutUseCase @Inject constructor(
    private val clientDao: ClientDao,
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): UseCase<Unit, Unit>(dispatchers.io) {

    override suspend fun execute(params: Unit) {
        clientDao.remove()
        settingsDataStore.removeValues(
            PreferenceKey.UserToken,
            PreferenceKey.UserId,
            PreferenceKey.PairedUser
        )
    }
}
