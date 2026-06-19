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
): UseCase<RegisterUseCase.Params, Unit>(dispatchers.io) {

    override suspend fun execute(params: Params) {
        val formattedPhone = String.format(Locale.getDefault(), FORMAT_PHONE_NUMBER, params.phone)

        handleResponse(
            request = {
                val request = AuthenticationRegisterRequest(
                    phone = formattedPhone,
                    name = params.name
                )
                networkService.authenticationRegister(request)
            },
            onSuccess = {
                val clientEntity = ClientEntity.Empty.copy(
                    phone = params.phone,
                    name = params.name,
                    codeResendTimer = System.currentTimeMillis()
                )
                clientDao.upsert(clientEntity)
            },
            onFailure = { error -> throw RegisterException(error.message) }
        )
    }

    data class Params(
        val phone: String,
        val name: String
    )
}
