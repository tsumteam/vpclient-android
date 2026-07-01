package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.LoyaltyCardDescription
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult
import ru.mercury.vpclient.shared.domain.mapper.loyaltyCardDescription
import javax.inject.Inject

// fixme
class LoyaltyCardTypesUseCase @Inject constructor(
    private val networkService: NetworkService,
    dispatchers: SharedDispatchers
): UseCase<Unit, List<LoyaltyCardDescription>>(dispatchers.io) {

    override suspend fun execute(params: Unit): List<LoyaltyCardDescription> {
        return handleResponseResult {
            networkService.loyaltyCardTypes()
        }.getOrThrow()
            .items
            .orEmpty()
            .map { cardType -> cardType.loyaltyCardDescription }
    }
}
