package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.persistence.database.dao.ClientDao
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val clientDao: ClientDao,
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): UseCase<Unit, Unit>(dispatchers.io) {

    override suspend fun execute(params: Unit) {
        handleResponse(
            request = { networkService.authenticationLogout() },
            onSuccess = {
                clientDao.remove()
                settingsDataStore.removeValues(
                    PreferenceKey.UserToken,
                    PreferenceKey.UserId,
                    PreferenceKey.PairedUser
                )
                settingsDataStore.removeValue(PreferenceKey.UseDiginetica)
            },
            onFailure = { error -> throw LogoutException(error.message) }
        )
    }

    data class LogoutException(
        override val message: String
    ): ClientException(message)
}
