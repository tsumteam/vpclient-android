package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.request.VerifyLinkCardRequest
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult
import javax.inject.Inject

// fixme
class VerifyLoyaltyCardUseCase @Inject constructor(
    private val networkService: NetworkService,
    dispatchers: SharedDispatchers
): UseCase<VerifyLoyaltyCardUseCase.Params, Unit>(dispatchers.io) {

    override suspend fun execute(params: Params) {
        val response = handleResponseResult {
            val request = VerifyLinkCardRequest(
                loyaltyCardNumber = params.cardNumber,
                smsCode = params.code
            )
            networkService.loyaltyVerifyLink(request = request)
        }.getOrThrow()

        response.error?.let { error ->
            throw VerifyLoyaltyCardException((error.display ?: error.msg).orEmpty().ifEmpty { "Неверный код" })
        }
    }

    data class Params(
        val cardNumber: String,
        val code: String
    )

    data class VerifyLoyaltyCardException(
        override val message: String
    ): ClientException(message)
}
