package ru.mercury.vpclient.shared.domain.repository.impl

import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.response.CurrentUserResponse
import ru.mercury.vpclient.shared.data.persistence.database.dao.ClientDao
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult
import ru.mercury.vpclient.shared.domain.repository.AuthenticationRepository
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val networkService: NetworkService,
    private val settingsDataStore: SettingsDataStore,
    private val clientDao: ClientDao
): AuthenticationRepository {

    override suspend fun currentUser(): CurrentUserResponse {
        val currentUser = handleResponseResult {
            networkService.userCurrentUser()
        }.getOrThrow()
        settingsDataStore.setValue(PreferenceKey.UserId, currentUser.code.orEmpty())
        return currentUser
    }

    override suspend fun userId(): String {
        return settingsDataStore.getValue(PreferenceKey.UserId).orEmpty()
    }

    override suspend fun deleteProfile() {
        handleResponse(
            request = { networkService.userProfileDelete() },
            onSuccess = { logout() },
            onEmpty = { logout() }
        )
    }

    override suspend fun logout() {
        clientDao.remove()
        settingsDataStore.removeValues(
            PreferenceKey.UserToken,
            PreferenceKey.UserId,
            PreferenceKey.PairedUser
        )

        /*handleResponse(
            request = {
                networkService.authenticationLogout()
            },
            onSuccess = {
                clientDao.remove()
                settingsDataStore.removeValue(PreferenceKey.UserToken)
            },
            onFailure = { error -> throw LogoutException(error.message) }
        )*/
    }
}
