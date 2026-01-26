package ru.mercury.vpclient.core.repository.impl

import ru.mercury.vpclient.core.network.NetworkService
import ru.mercury.vpclient.core.network.request.AuthenticationContinueLoginRequest
import ru.mercury.vpclient.core.network.request.AuthenticationLoginRequest
import ru.mercury.vpclient.core.network.request.AuthenticationRegisterRequest
import ru.mercury.vpclient.core.persistence.database.AppDatabase
import ru.mercury.vpclient.core.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.core.repository.AuthenticationRepository
import javax.inject.Inject
import javax.inject.Provider

class AuthenticationRepositoryImpl @Inject constructor(
    private val networkService: NetworkService,
    private val settingsDataStore: SettingsDataStore,
    private val appDatabase: Provider<AppDatabase>
): AuthenticationRepository {

    override suspend fun register(phone: String, name: String): String {
        return networkService.authenticationRegister(
            request = AuthenticationRegisterRequest(phone = phone, name = name)
        )
    }

    override suspend fun login(phone: String): String {
        return networkService.authenticationLogin(
            request = AuthenticationLoginRequest(phone)
        )
    }

    override suspend fun continueLogin(phone: String, code: String): String {
        return networkService.authenticationContinueLogin(
            request = AuthenticationContinueLoginRequest(phone = phone, code = code)
        )
    }

    override suspend fun logout() {
        return networkService.authenticationLogout()
    }
}
