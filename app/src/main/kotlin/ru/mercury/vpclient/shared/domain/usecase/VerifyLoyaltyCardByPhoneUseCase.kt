package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.FORMAT_PHONE_NUMBER
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.request.LoyaltyVerifyByPhoneRequest
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult
import java.util.Locale
import javax.inject.Inject

// fixme
class VerifyLoyaltyCardByPhoneUseCase @Inject constructor(
    private val networkService: NetworkService,
    dispatchers: SharedDispatchers
): UseCase<VerifyLoyaltyCardByPhoneUseCase.Params, Unit>(dispatchers.io) {

    override suspend fun execute(params: Params) {
        val response = handleResponseResult {
            val request = LoyaltyVerifyByPhoneRequest(
                phone = String.format(Locale.getDefault(), FORMAT_PHONE_NUMBER, params.phone),
                code = params.code
            )
            networkService.loyaltyVerifyByPhone(request = request)
        }.getOrThrow()

        response.error?.let { error ->
            throw VerifyLoyaltyCardByPhoneException((error.display ?: error.msg).orEmpty().ifEmpty { "Неверный код" })
        }
    }

    data class Params(
        val phone: String,
        val code: String
    )

    data class VerifyLoyaltyCardByPhoneException(
        override val message: String
    ): ClientException(message)
}
