package ru.mercury.vpclient.shared.domain.repository.impl

import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.data.FORMAT_PHONE_NUMBER
import ru.mercury.vpclient.shared.data.error.ContinueLoginException
import ru.mercury.vpclient.shared.data.error.LoginException
import ru.mercury.vpclient.shared.data.error.RegisterException
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.request.AuthenticationContinueLoginRequest
import ru.mercury.vpclient.shared.data.network.request.AuthenticationLoginRequest
import ru.mercury.vpclient.shared.data.network.request.AuthenticationRegisterRequest
import ru.mercury.vpclient.shared.data.persistence.database.dao.ClientDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.ClientEntity
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.repository.AuthenticationRepository
import java.util.Locale
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val networkService: NetworkService,
    private val settingsDataStore: SettingsDataStore,
    private val clientDao: ClientDao
): AuthenticationRepository {

    override val clientEntityFlow: Flow<ClientEntity> = clientDao.selectFlow()

    override suspend fun register(phone: String, name: String) {
        val formattedPhone = String.format(Locale.getDefault(), FORMAT_PHONE_NUMBER, phone)

        handleResponse(
            request = {
                val request = AuthenticationRegisterRequest(phone = formattedPhone, name = name)
                networkService.authenticationRegister(request)
            },
            onSuccess = {
                val clientEntity = ClientEntity.Empty.copy(
                    phone = phone,
                    name = name,
                    codeResendTimer = System.currentTimeMillis()
                )
                clientDao.upsert(clientEntity)
            },
            onFailure = { error -> throw RegisterException(error.message) }
        )
    }

    override suspend fun login(phone: String) {
        val formattedPhone = String.format(Locale.getDefault(), FORMAT_PHONE_NUMBER, phone)

        handleResponse(
            request = {
                val request = AuthenticationLoginRequest(formattedPhone)
                networkService.authenticationLogin(request)
            },
            onSuccess = {
                val clientEntity = ClientEntity.Empty.copy(
                    phone = phone,
                    codeResendTimer = System.currentTimeMillis()
                )
                clientDao.upsert(clientEntity)
            },
            onFailure = { error -> throw LoginException(error.message) }
        )
    }

    override suspend fun continueLogin(code: String) {
        val clientEntity = clientDao.select()
        val formattedPhone = String.format(Locale.getDefault(), FORMAT_PHONE_NUMBER, clientEntity.phone)

        handleResponse(
            request = {
                val request = AuthenticationContinueLoginRequest(
                    phone = formattedPhone,
                    code = code
                )
                networkService.authenticationContinueLogin(request)
            },
            onSuccess = {
                settingsDataStore.setValue(PreferenceKey.UserToken, it.token.orEmpty())
                val currentUser = networkService.userCurrentUser()
                val userId = currentUser.data?.id?.toString().orEmpty()
                settingsDataStore.setValue(PreferenceKey.UserId, userId)
                val activeEmployee = networkService.clientActiveEmployee()
                val activeEmployeeId = activeEmployee.data?.employeeId.orEmpty()
                settingsDataStore.setValue(PreferenceKey.PairedUser, activeEmployeeId)
            },
            onFailure = { error -> throw ContinueLoginException(error.message) }
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

    override suspend fun resendCode() {
        val clientEntity = clientDao.select()
        val formattedPhone = String.format(Locale.getDefault(), FORMAT_PHONE_NUMBER, clientEntity.phone)

        when {
            clientEntity.name.isEmpty() -> {
                handleResponse(
                    request = {
                        val request = AuthenticationLoginRequest(formattedPhone)
                        networkService.authenticationLogin(request)
                    },
                    onSuccess = {
                        clientDao.update(clientEntity.copy(codeResendTimer = System.currentTimeMillis()))
                    },
                    onFailure = { error -> throw LoginException(error.message) }
                )
            }
            else -> {
                handleResponse(
                    request = {
                        val request = AuthenticationRegisterRequest(phone = formattedPhone, name = clientEntity.name)
                        networkService.authenticationRegister(request)
                    },
                    onSuccess = {
                        clientDao.update(clientEntity.copy(codeResendTimer = System.currentTimeMillis()))
                    },
                    onFailure = { error -> throw RegisterException(error.message) }
                )
            }
        }
    }
}
