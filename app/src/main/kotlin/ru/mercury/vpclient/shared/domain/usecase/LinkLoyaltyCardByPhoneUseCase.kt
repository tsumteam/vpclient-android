@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.CancellationException
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.FORMAT_PHONE_NUMBER
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.request.LoyaltyLinkByPhoneRequest
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult
import java.util.Locale
import javax.inject.Inject

// fixme
class LinkLoyaltyCardByPhoneUseCase @Inject constructor(
    private val networkService: NetworkService,
    dispatchers: SharedDispatchers
): UseCase<String, Boolean>(dispatchers.io) {

    override suspend fun execute(phone: String): Boolean {
        val response = try {
            handleResponseResult {
                val request = LoyaltyLinkByPhoneRequest(
                    clientPhone = String.format(Locale.getDefault(), FORMAT_PHONE_NUMBER, phone)
                )
                networkService.loyaltyLinkByPhone(request = request)
            }.getOrThrow()
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            throw LinkLoyaltyCardByPhoneException(
                exception.message.orEmpty().ifEmpty { "Не удалось привязать карту" }
            )
        }

        response.error?.let { error ->
            throw LinkLoyaltyCardByPhoneException((error.display ?: error.msg).orEmpty().ifEmpty { "Не удалось привязать карту" })
        }

        return response.isNeedVerification ?: throw LinkLoyaltyCardByPhoneException("Не удалось привязать карту")
    }

    data class LinkLoyaltyCardByPhoneException(
        override val message: String
    ): ClientException(message)
}
