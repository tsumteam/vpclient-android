package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.ProfileOrder
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.request.ProfileOrdersSalesRequest
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult
import ru.mercury.vpclient.shared.domain.mapper.toProfileOrder
import javax.inject.Inject

// fixme
class ProfileOrdersUseCase @Inject constructor(
    private val networkService: NetworkService,
    dispatchers: SharedDispatchers
): UseCase<ProfileOrdersUseCase.Params, List<ProfileOrder>>(dispatchers.io) {

    override suspend fun execute(params: Params): List<ProfileOrder> {
        val response = handleResponseResult {
            val request = ProfileOrdersSalesRequest(
                clientId = params.clientId,
                limit = params.limit,
                offset = params.offset
            )
            networkService.sales(
                request = request,
                limit = params.limit,
                offset = params.offset
            )
        }.getOrThrow()

        return response.items.orEmpty().mapNotNull { item -> item.toProfileOrder() }
    }

    data class Params(
        val clientId: String,
        val limit: Int,
        val offset: Int
    )
}
