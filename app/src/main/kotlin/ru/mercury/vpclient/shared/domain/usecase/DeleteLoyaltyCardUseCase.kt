@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.request.DeleteLinkedCardRequest
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult
import javax.inject.Inject

// fixme
class DeleteLoyaltyCardUseCase @Inject constructor(
    private val networkService: NetworkService,
    dispatchers: SharedDispatchers
): UseCase<String, Unit>(dispatchers.io) {

    override suspend fun execute(cardNumber: String) {
        handleResponseResult {
            val request = DeleteLinkedCardRequest(loyaltyCardNumber = cardNumber)
            networkService.loyaltyDelete(request = request)
        }.getOrThrow()
    }
}
