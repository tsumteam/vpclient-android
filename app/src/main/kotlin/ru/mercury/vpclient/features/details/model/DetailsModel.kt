package ru.mercury.vpclient.features.details.model

import ru.mercury.vpclient.shared.data.entity.DetailsField
import ru.mercury.vpclient.shared.data.entity.SizeSelectorState
import ru.mercury.vpclient.shared.data.entity.SizeState
import ru.mercury.vpclient.shared.mvi.Model
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductRelatedItemEntity

// fixme

data class DetailsModel(
    val productEntity: ProductEntity = ProductEntity.Empty,
    val selectedSizeId: String? = null,
    val selectedOtherColorIndex: Int? = null
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

    val hasVideo: Boolean
        get() = selectedColorVideoUrl != null

    val isSizePickerVisible: Boolean
        get() = productEntity.availableSizes?.items?.isNotEmpty() == true

    val isProductColorImagesBoxVisible: Boolean
        get() = productEntity.otherColors.isNotEmpty()

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
        additionalColorPhotoUrls = emptyList(),
        position = position
    )
}
