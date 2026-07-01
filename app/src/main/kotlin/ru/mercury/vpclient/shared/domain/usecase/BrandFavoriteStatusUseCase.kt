package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult
import javax.inject.Inject

// fixme
class BrandFavoriteStatusUseCase @Inject constructor(
    private val networkService: NetworkService,
    dispatchers: SharedDispatchers
): UseCase<BrandFavoriteStatusUseCase.Params, Boolean>(dispatchers.io) {

    override suspend fun execute(params: Params): Boolean {
        return handleResponseResult {
            networkService.catalogBrandsIsFavorite(params.brandId, params.categoryId)
        }.getOrDefault(false)
    }

    data class Params(
        val brandId: Int,
        val categoryId: Int
    )
}
