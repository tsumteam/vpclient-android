@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import javax.inject.Inject

class CompilationsClientLookByIdToBasketUseCase @Inject constructor(
    private val networkService: NetworkService,
    dispatchers: SharedDispatchers
): UseCase<Int, Unit>(dispatchers.io) {

    override suspend fun execute(lookId: Int) {
        handleResponse(
            request = { networkService.compilationsClientLookByLookIdToBasket(lookId) },
            onFailure = { error -> throw CompilationsClientLookByIdToBasketException(error.message) }
        )
    }

    data class CompilationsClientLookByIdToBasketException(
        override val message: String
    ): ClientException(message)
}
