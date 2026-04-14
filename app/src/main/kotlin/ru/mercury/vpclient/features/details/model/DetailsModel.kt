package ru.mercury.vpclient.features.details.model

import ru.mercury.vpclient.shared.entity.DetailsField
import ru.mercury.vpclient.shared.entity.SizeSelectorState
import ru.mercury.vpclient.shared.entity.SizeState
import ru.mercury.vpclient.shared.mvi.Model
import ru.mercury.vpclient.shared.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.persistence.database.entity.ProductEntity
import ru.mercury.vpclient.shared.persistence.database.entity.ProductRelatedItemEntity

// fixme

data class DetailsModel(
    val productEntity: ProductEntity = ProductEntity.Empty,
    val selectedSizeId: String? = null
): Model {

    val isLoading: Boolean
        get() = productEntity == ProductEntity.Empty

    val hasVideo: Boolean
        get() = productEntity.urlItemVideo != null

    val isSizePickerVisible: Boolean
        get() = productEntity.availableSizes?.items?.isNotEmpty() == true

    val isProductColorImagesBoxVisible: Boolean
        get() = productEntity.otherColorImageUrls.isNotEmpty()

    val descriptionText: String?
        get() = listOf(productEntity.artDescription, productEntity.longDescription)
            .firstOrNull { it.isNullOrBlank().not() }

    val isDescriptionTextVisible: Boolean
        get() = !descriptionText.isNullOrEmpty()

    val isWearWithBoxVisible: Boolean
        get() = wearWithProducts.isNotEmpty()

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

private fun String.toField(factory: (String) -> DetailsField): DetailsField? {
    return trim().takeIf { it.isNotEmpty() }?.let(factory)
}

private fun ProductRelatedItemEntity.toCatalogFilterProductsEntity(position: Int): CatalogFilterProductsEntity {
    return CatalogFilterProductsEntity(
        categoryId = 0,
        titleCategoryId = 0,
        id = id,
        itemId = itemId,
        colorId = colorId,
        name = name.orEmpty(),
        price = price,
        priceWithoutDiscount = priceWithoutDiscount,
        brand = brand.orEmpty(),
        urlBrandLogo = urlBrandLogo,
        imageUrl = imageUrl.orEmpty(),
        imageUrls = imageUrls,
        position = position
    )
}
