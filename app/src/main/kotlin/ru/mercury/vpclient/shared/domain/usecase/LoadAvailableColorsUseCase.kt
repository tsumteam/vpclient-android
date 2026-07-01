@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.ProductAvailableColor
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.request.AvailableColorsRequest
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult
import javax.inject.Inject

// fixme
class LoadAvailableColorsUseCase @Inject constructor(
    private val networkService: NetworkService,
    dispatchers: SharedDispatchers
): UseCase<CartProduct, List<ProductAvailableColor>>(dispatchers.io) {

    override suspend fun execute(product: CartProduct): List<ProductAvailableColor> {
        if (product.itemId.isEmpty() || product.sizeId.isEmpty()) {
            return emptyList()
        }

        val response = handleResponseResult {
            networkService.catalogAvailableColors(
                AvailableColorsRequest(
                    itemId = product.itemId,
                    sizeId = product.sizeId
                )
            )
        }.getOrThrow()

        return response.items.orEmpty().mapNotNull { color ->
            val colorId = color.colorId ?: return@mapNotNull null
            ProductAvailableColor(
                id = colorId,
                name = color.colorFullName.orEmpty().ifBlank { colorId },
                hex = color.colorHex.orEmpty(),
                selected = colorId == product.colorId
            )
        }
    }
}
