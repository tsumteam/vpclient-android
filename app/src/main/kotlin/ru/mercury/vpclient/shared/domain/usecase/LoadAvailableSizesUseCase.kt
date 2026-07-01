@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.request.AvailableSizesRequest
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductAvailableSizeEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductAvailableSizesEntity
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult
import javax.inject.Inject

// fixme
class LoadAvailableSizesUseCase @Inject constructor(
    private val networkService: NetworkService,
    dispatchers: SharedDispatchers
): UseCase<CartProduct, ProductAvailableSizesEntity>(dispatchers.io) {

    override suspend fun execute(product: CartProduct): ProductAvailableSizesEntity {
        if (product.itemId.isEmpty() || product.colorId.isEmpty()) {
            return ProductAvailableSizesEntity(
                items = emptyList(),
                countryCode = null,
                sizeTableTitle = null,
                sizeTableUrl = null
            )
        }

        val response = handleResponseResult {
            networkService.catalogAvailableSizes(
                AvailableSizesRequest(
                    itemId = product.itemId,
                    colorId = product.colorId
                )
            )
        }.getOrThrow()

        return ProductAvailableSizesEntity(
            items = response.items.orEmpty().mapNotNull { size ->
                val sizeId = size.sizeId ?: return@mapNotNull null
                ProductAvailableSizeEntity(
                    sizeId = sizeId,
                    russianSize = size.sizeFullName,
                    sizeFullName = size.sizeFullName,
                    inStock = size.inStock == true
                )
            },
            countryCode = null,
            sizeTableTitle = response.sizeTableTitle,
            sizeTableUrl = response.sizeTableUrl
        )
    }
}
