package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.response.CurrentUserResponse
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult
import javax.inject.Inject

// fixme
class CurrentUserUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): UseCase<Unit, CurrentUserResponse>(dispatchers.io) {

    override suspend fun execute(params: Unit): CurrentUserResponse {
        val currentUser = handleResponseResult {
            networkService.userCurrentUser()
        }.getOrThrow()
        settingsDataStore.setValue(PreferenceKey.UserId, currentUser.code.orEmpty())
        return currentUser
    }
}
