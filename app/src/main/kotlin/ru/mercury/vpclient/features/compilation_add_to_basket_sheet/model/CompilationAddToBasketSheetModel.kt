package ru.mercury.vpclient.features.compilation_add_to_basket_sheet.model

import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductAvailableSizesEntity
import ru.mercury.vpclient.shared.ui.components.details.SizeSelectorState
import ru.mercury.vpclient.shared.ui.components.details.SizeState

data class CompilationAddToBasketSheetModel(
    val productEntities: List<CatalogFilterProductsEntity>,
    val selectedProductIds: Set<String>,
    val availableSizes: Map<String, ProductAvailableSizesEntity> = emptyMap(),
    val oneSizeProductIds: Set<String> = emptySet(),
    val isLoading: Boolean
) {

    val isAddToBasketButtonEnabled: Boolean
        get() = !isLoading && selectedProductIds.isNotEmpty()

    fun sizeSelectorState(entity: CatalogFilterProductsEntity): SizeSelectorState? {
        val productAvailableSizes = availableSizes[entity.id] ?: entity.availableSizes ?: return null
        val items = productAvailableSizes.items
        val isOneSize = when {
            availableSizes.containsKey(entity.id) -> entity.id in oneSizeProductIds
            else -> entity.isOneSize
        }
        if (items.isEmpty() || isOneSize) {
            return null
        }

        val topText = productAvailableSizes.countryCode
            ?.takeIf { countryCode -> countryCode.isNotBlank() }
            ?: items.firstOrNull()
                ?.sizeId
                ?.substringBefore(" ")
                ?.takeIf { countryCode -> countryCode != items.first().sizeId }
                .orEmpty()
        val bottomText = when {
            items.any { size -> size.russianSize.isNullOrBlank().not() } -> "RU"
            else -> ""
        }

        return SizeSelectorState(
            sizes = items.map { size ->
                val topSizeText = when {
                    topText.isNotBlank() -> size.sizeId.removePrefix("$topText ").trim()
                    else -> size.sizeId
                }
                SizeState(
                    topText = topSizeText,
                    bottomText = size.russianSize ?: "-",
                    selected = false,
                    enabled = size.inStock
                )
            },
            topText = topText,
            bottomText = bottomText,
            isSizeTableVisible = productAvailableSizes.sizeTableTitle.isNullOrBlank().not() &&
                productAvailableSizes.sizeTableUrl.isNullOrBlank().not(),
            isSizeSelectTextVisible = false
        )
    }
}
