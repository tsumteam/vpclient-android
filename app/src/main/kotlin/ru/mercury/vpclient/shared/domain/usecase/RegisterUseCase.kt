package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.FORMAT_PHONE_NUMBER
import ru.mercury.vpclient.shared.data.error.RegisterException
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.request.AuthenticationRegisterRequest
import ru.mercury.vpclient.shared.data.persistence.database.dao.ClientDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.ClientEntity
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import java.util.Locale
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val clientDao: ClientDao,
    dispatchers: SharedDispatchers
): UseCase<RegisterUseCase.Parameters, Unit>(dispatchers.io) {

    override suspend fun execute(parameters: Parameters) {
        val formattedPhone = String.format(Locale.getDefault(), FORMAT_PHONE_NUMBER, parameters.phone)

        handleResponse(
            request = {
                val request = AuthenticationRegisterRequest(
                    phone = formattedPhone,
                    name = parameters.name
                )
                networkService.authenticationRegister(request)
            },
            onSuccess = {
                val clientEntity = ClientEntity.Empty.copy(
                    phone = parameters.phone,
                    name = parameters.name,
                    codeResendTimer = System.currentTimeMillis()
                )
                clientDao.upsert(clientEntity)
            },
            onFailure = { error -> throw RegisterException(error.message) }
        )
    }

    data class Parameters(
        val phone: String,
        val name: String
    )
}
