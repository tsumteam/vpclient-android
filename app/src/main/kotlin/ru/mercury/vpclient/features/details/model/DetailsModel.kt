package ru.mercury.vpclient.features.details.model

import ru.mercury.vpclient.shared.data.entity.DetailsField
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductEntity
import ru.mercury.vpclient.shared.domain.mapper.hasFittingBadge
import ru.mercury.vpclient.shared.domain.mapper.hasMessengerBadge
import ru.mercury.vpclient.shared.domain.mapper.toCatalogFilterProductsEntity
import ru.mercury.vpclient.shared.domain.mapper.toField
import ru.mercury.vpclient.shared.mvi.Model
import ru.mercury.vpclient.shared.ui.components.details.SizeSelectorState
import ru.mercury.vpclient.shared.ui.components.details.SizeState
import ru.mercury.vpclient.shared.ui.theme.ClientStrings

data class DetailsModel(
    val productEntity: ProductEntity = ProductEntity.Empty,
    val selectedSizeId: String? = null,
    val selectedOtherColorIndex: Int? = null,
    val isSizePickerSheetVisible: Boolean = false,
    val isWearWithSheetVisible: Boolean = false,
    val isMessageSheetVisible: Boolean = false,
    val isCartAddedSheetVisible: Boolean = false,
    val basketProductIds: Set<String> = emptySet(),
    val basketProductKeys: Set<String> = emptySet(),
    val cartCount: Int = 0,
    val cartBadge: Int = 0,
    val fittingCount: Int = 0,
    val activeEmployee: EmployeeEntity = EmployeeEntity.Empty
): Model {

    val pagerImageUrls: List<String>
        get() {
            val idx = selectedOtherColorIndex ?: return productEntity.colorImageUrls
            return productEntity.otherColors.getOrNull(idx)?.imageUrls.orEmpty()
        }

    val selectedColorVideoUrl: String?
        get() {
            val idx = selectedOtherColorIndex ?: return productEntity.urlItemVideo
            return productEntity.otherColors.getOrNull(idx)?.urlItemVideo
        }

    val selectorColorImageUrls: List<String>
        get() {
            val idx = selectedOtherColorIndex
            val mainImage = productEntity.colorImageUrls.firstOrNull()
            return productEntity.otherColors.mapIndexed { i, color ->
                if (i == idx) mainImage ?: color.imageUrls.firstOrNull() ?: ""
                else color.imageUrls.firstOrNull() ?: ""
            }
        }

    val isLoading: Boolean
        get() = productEntity == ProductEntity.Empty

    val cartText: String
        get() = when {
            cartCount > 0 -> cartCount.toString()
            else -> ""
        }

    val isCartBadgeVisible: Boolean
        get() = cartBadge > 0

    val fittingText: String
        get() = if (fittingCount > 0) fittingCount.toString() else ""

    val isFittingButtonVisible: Boolean
        get() = fittingCount > 0

    val isFittingBadgeVisible: Boolean
        get() = activeEmployee.hasFittingBadge

    val isMessengerBadgeVisible: Boolean
        get() = activeEmployee.hasMessengerBadge

    val hasVideo: Boolean
        get() = selectedColorVideoUrl != null

    val isNoSizeProduct: Boolean
        get() {
            val items = productEntity.availableSizes?.items.orEmpty()
            return items.size == 1 &&
                items.first().sizeId.equals(NO_SIZE_VALUE, ignoreCase = true) &&
                items.first().russianSize.equals(NO_SIZE_VALUE, ignoreCase = true)
        }

    val isSizePickerVisible: Boolean
        get() = productEntity.availableSizes?.items?.isNotEmpty() == true && !isNoSizeProduct

    val isNoSizeProductInStock: Boolean
        get() = isNoSizeProduct && productEntity.availableSizes?.items?.firstOrNull()?.inStock == true

    val noSizeAvailabilityText: Int?
        get() = when {
            !isNoSizeProduct -> null
            isNoSizeProductInStock -> ClientStrings.DetailsSizeInStock
            else -> ClientStrings.DetailsSold
        }

    val addToBasketButtonText: Int
        get() = when {
            isNoSizeProductInStock -> ClientStrings.DetailsAddToBasket
            isNoSizeProduct -> ClientStrings.DetailsPickAlternativeInBasket
            sizePickerState.selectedSize?.enabled == false -> ClientStrings.DetailsPickAlternativeInBasket
            else -> ClientStrings.DetailsAddToBasket
        }

    val isProductColorImagesBoxVisible: Boolean
        get() = productEntity.otherColors.isNotEmpty()

    val descriptionText: String?
        get() = listOf(productEntity.artDescription, productEntity.longDescription)
            .firstOrNull { it.isNullOrBlank().not() }

    val isDescriptionTextVisible: Boolean
        get() = !descriptionText.isNullOrEmpty()

    val isWearWithButtonVisible: Boolean
        get() = productEntity.hasWearWith && productEntity.wearWithButtonEnabled

    val isWearWithBoxVisible: Boolean
        get() = productEntity.hasWearWith && wearWithProducts.isNotEmpty()

    val isCompleteSetBoxVisible: Boolean
        get() = completeSetProducts.isNotEmpty()

    val detailFields: List<DetailsField>
        get() = listOfNotNull(
            productEntity.brand?.toField { DetailsField.Brand(it) },
            productEntity.colorName?.toField { DetailsField.Color(it) },
            productEntity.productionStructure?.toField { DetailsField.Composition(it) },
            productEntity.country?.toField { DetailsField.Country(it) },
            productEntity.itemId?.toField { DetailsField.ItemId(it) },
            productEntity.article?.toField { DetailsField.Article(it) },
            productEntity.technicalDescription?.toField { DetailsField.Measurements(it) }
        )

    val wearWithProducts: List<CatalogFilterProductsEntity>
        get() = productEntity.wearWithProducts.mapIndexed { index, item -> item.toCatalogFilterProductsEntity(index) }

    val completeSetProducts: List<CatalogFilterProductsEntity>
        get() = productEntity.completeSetProducts.mapIndexed { index, item -> item.toCatalogFilterProductsEntity(index) }

    fun isProductInBasket(entity: CatalogFilterProductsEntity): Boolean {
        return "${entity.itemId}:${entity.colorId}:" in basketProductKeys ||
            "${entity.itemId}:${entity.colorId}:NS" in basketProductKeys
    }

    val sizePickerState: SizeSelectorState
        get() {
            val availableSizes = productEntity.availableSizes ?: return SizeSelectorState.Empty
            val currentSelectedSizeId = selectedSizeId
            return SizeSelectorState(
                sizes = availableSizes.items.map { size ->
                    SizeState(
                        topText = size.sizeId.uppercase(),
                        bottomText = size.russianSize ?: "-",
                        selected = size.sizeId == currentSelectedSizeId,
                        enabled = size.inStock
                    )
                },
                topText = availableSizes.countryCode.orEmpty(),
                bottomText = "RU",
                isSizeTableVisible = !availableSizes.sizeTableTitle.isNullOrEmpty() && !availableSizes.sizeTableUrl.isNullOrEmpty()
            )
        }
}

private const val NO_SIZE_VALUE = "NS"
