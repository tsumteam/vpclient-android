@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.request.AvailableMultipleSizesRequest
import ru.mercury.vpclient.shared.data.network.request.AvailableMultipleSizesRequestItem
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductAvailableSizesEntity
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult
import ru.mercury.vpclient.shared.domain.mapper.toProductAvailableSizesEntity
import ru.mercury.vpclient.shared.domain.usecase.CatalogAvailableForMultipleSizesUseCase.ProductSizes
import javax.inject.Inject

class CatalogAvailableForMultipleSizesUseCase @Inject constructor(
    private val networkService: NetworkService,
    dispatchers: SharedDispatchers
): UseCase<List<CatalogFilterProductsEntity>, List<ProductSizes>>(dispatchers.io) {

    override suspend fun execute(entities: List<CatalogFilterProductsEntity>): List<ProductSizes> {
        val requestEntities = entities.filter { entity ->
            entity.id.isNotBlank() &&
                entity.itemId.isNotBlank()
        }
        if (requestEntities.isEmpty()) {
            return emptyList()
        }

        val response = handleResponseResult {
            val request = AvailableMultipleSizesRequest(
                items = requestEntities.map { entity ->
                    AvailableMultipleSizesRequestItem(
                        itemId = entity.itemId,
                        colorId = entity.colorId
                    )
                }
            )
            networkService.catalogAvailableForMultipleSizes(request)
        }.getOrElse { exception ->
            throw CatalogAvailableForMultipleSizesException(exception.message.orEmpty())
        }

        return response.items.orEmpty().mapIndexedNotNull { index, sizesResponse ->
            val entity = requestEntities.firstOrNull { entity ->
                entity.itemId == sizesResponse.itemId &&
                    entity.colorId.takeIf { colorId -> colorId.isNotBlank() } ==
                    sizesResponse.colorId?.takeIf { colorId -> colorId.isNotBlank() }
            } ?: requestEntities.getOrNull(index) ?: return@mapIndexedNotNull null
            ProductSizes(
                productId = entity.id,
                availableSizes = sizesResponse.toProductAvailableSizesEntity(),
                oneSize = sizesResponse.oneSize == true
            )
        }
    }

    data class ProductSizes(
        val productId: String,
        val availableSizes: ProductAvailableSizesEntity,
        val oneSize: Boolean
    )

    data class CatalogAvailableForMultipleSizesException(override val message: String): Exception(message)
}
