package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.FORMAT_PHONE_NUMBER
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.request.AuthenticationLoginRequest
import ru.mercury.vpclient.shared.data.network.request.AuthenticationRegisterRequest
import ru.mercury.vpclient.shared.data.persistence.database.dao.ClientDao
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import java.util.Locale
import javax.inject.Inject

class AuthResendCodeUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val clientDao: ClientDao,
    dispatchers: SharedDispatchers
): UseCase<Unit, Unit>(dispatchers.io) {

    override suspend fun execute(params: Unit) {
        val clientEntity = clientDao.selectNotNull()
        val formattedPhone = String.format(Locale.getDefault(), FORMAT_PHONE_NUMBER, clientEntity.phone)

        when {
            clientEntity.name.isEmpty() -> {
                handleResponse(
                    request = {
                        val request = AuthenticationLoginRequest(formattedPhone)
                        networkService.authenticationLogin(request)
                    },
                    onSuccess = {
                        val updatedClientEntity = clientEntity.copy(
                            codeResendTimer = System.currentTimeMillis()
                        )
                        clientDao.update(updatedClientEntity)
                    },
                    onFailure = { error -> throw AuthResendCodeException(error.message) }
                )
            }
            else -> {
                handleResponse(
                    request = {
                        val request = AuthenticationRegisterRequest(
                            phone = formattedPhone,
                            name = clientEntity.name
                        )
                        networkService.authenticationRegister(request)
                    },
                    onSuccess = {
                        val updatedClientEntity = clientEntity.copy(
                            codeResendTimer = System.currentTimeMillis()
                        )
                        clientDao.update(updatedClientEntity)
                    },
                    onFailure = { error -> throw AuthResendCodeException(error.message) }
                )
            }
        }
    }

    data class AuthResendCodeException(
        override val message: String
    ): ClientException(message)
}
