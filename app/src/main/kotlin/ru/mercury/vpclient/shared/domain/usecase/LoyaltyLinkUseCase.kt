@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.error.ClientException
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.request.LinkCardRequest
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import javax.inject.Inject

class LoyaltyLinkUseCase @Inject constructor(
    private val networkService: NetworkService,
    dispatchers: SharedDispatchers
): UseCase<String, Unit>(dispatchers.io) {

    override suspend fun execute(loyaltyCardNumber: String) {
        handleResponse(
            request = {
                val request = LinkCardRequest(loyaltyCardNumber)
                networkService.loyaltyLink(request)
            },
            onSuccess = { response ->
                if (response.error != null) {
                    throw LoyaltyLinkException((response.error.display ?: response.error.msg).orEmpty())
                }
            },
            onFailure = { error -> throw LoyaltyLinkException(error.message) }
        )
    }

    data class LoyaltyLinkException(
        override val message: String
    ): ClientException(message)
}
