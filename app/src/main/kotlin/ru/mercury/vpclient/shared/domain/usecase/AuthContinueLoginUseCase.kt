package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.FORMAT_PHONE_NUMBER
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.request.AuthenticationContinueLoginRequest
import ru.mercury.vpclient.shared.data.persistence.database.dao.ClientDao
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import java.util.Locale
import javax.inject.Inject

class AuthContinueLoginUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val settingsDataStore: SettingsDataStore,
    private val clientDao: ClientDao,
    dispatchers: SharedDispatchers
): UseCase<String, Unit>(dispatchers.io) {

    override suspend fun execute(params: String) {
        val clientEntity = clientDao.selectNotNull()
        val formattedPhone = String.format(Locale.getDefault(), FORMAT_PHONE_NUMBER, clientEntity.phone)

        handleResponse(
            request = {
                val request = AuthenticationContinueLoginRequest(
                    phone = formattedPhone,
                    code = params
                )
                networkService.authenticationContinueLogin(request)
            },
            onSuccess = { tokenResponse ->
                settingsDataStore.setValue(PreferenceKey.UserToken, tokenResponse.token.orEmpty())

                val currentUser = networkService.userCurrentUser()
                val userId = currentUser.data?.code.orEmpty()
                settingsDataStore.setValue(PreferenceKey.UserId, userId)

                val activeEmployee = networkService.clientActiveEmployee()
                val activeEmployeeId = activeEmployee.data?.employeeId.orEmpty()
                settingsDataStore.setValue(PreferenceKey.PairedUser, activeEmployeeId)
            },
            onFailure = { error -> throw AuthContinueLoginException(error.message) }
        )
    }

    data class AuthContinueLoginException(
        override val message: String
    ): ClientException(message)
}
