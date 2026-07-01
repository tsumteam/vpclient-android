@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.FORMAT_PHONE_NUMBER
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.request.AuthenticationLoginRequest
import ru.mercury.vpclient.shared.data.persistence.database.dao.ClientDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.ClientEntity
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import java.util.Locale
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val clientDao: ClientDao,
    dispatchers: SharedDispatchers
): UseCase<String, Unit>(dispatchers.io) {

    override suspend fun execute(phone: String) {
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

    data class LoginException(
        override val message: String
    ): ClientException(message)
}
