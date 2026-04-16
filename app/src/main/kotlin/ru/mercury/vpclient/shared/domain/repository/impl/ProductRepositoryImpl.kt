package ru.mercury.vpclient.shared.domain.repository.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.request.DetailCardRequest
import ru.mercury.vpclient.shared.data.network.response.CatalogProductSearchCardV2Response
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogFilterProductsDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.ProductDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductAvailableSizeEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductAvailableSizesEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductOtherColorEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductRelatedItemEntity
import ru.mercury.vpclient.shared.domain.repository.ProductRepository
import javax.inject.Inject

// fixme

class ProductRepositoryImpl @Inject constructor(
    private val networkService: NetworkService,
    private val catalogFilterProductsDao: CatalogFilterProductsDao,
    private val productDao: ProductDao
): ProductRepository {

    override fun productFlow(id: String): Flow<ProductEntity> {
        return productDao.selectFlow(id).map { it ?: ProductEntity.Empty }
    }

    override suspend fun loadProduct(id: String) {
        val catalogEntity = catalogFilterProductsDao.select(id) ?: return
        handleResponse(
            request = {
                networkService.catalogDetailedProduct(
                    DetailCardRequest(itemId = catalogEntity.itemId, colorId = catalogEntity.colorId)
                )
            },
            onSuccess = { response ->
                val selectedColor = response.colors?.let { colors ->
                    colors.find { it.colorId == catalogEntity.colorId }
                        ?: colors.find { it.isSelected == true }
                        ?: colors.firstOrNull()
                }
                val entity = ProductEntity(
                    id = id,
                    name = response.name,
                    itemId = response.itemId,
                    categoryId = response.categoryId,
                    brandId = response.brandId,
                    brand = response.brand,
                    colorName = listOfNotNull(selectedColor?.colorName, selectedColor?.colorId?.let { "($it)" })
                        .joinToString(separator = " ")
                        .takeIf { it.isNotEmpty() },
                    urlBrandLogo = response.urlBrandLogo,
                    article = response.article,
                    longDescription = response.longDescription,
                    artDescription = selectedColor?.artDescription,
                    productionStructure = response.productionStructure,
                    country = response.country,
                    shortDescription = response.shortDescription,
                    technicalDescription = response.technicalDescription,
                    ekttId = response.ekttId,
                    price = selectedColor?.price,
                    priceWithoutDiscount = selectedColor?.priceWithoutDiscount,
                    breadcrumbs = response.breadcrumbs.orEmpty(),
                    colorImageUrls = selectedColor?.imageUrls.orEmpty(),
                    otherColors = if ((response.colors?.size ?: 0) > 1) {
                        response.colors
                            .orEmpty()
                            .filter { it.colorId != selectedColor?.colorId }
                            .map { ProductOtherColorEntity(imageUrls = it.imageUrls.orEmpty(), urlItemVideo = it.urlItemVideo) }
                    } else {
                        emptyList()
                    },
                    urlItemVideo = selectedColor?.urlItemVideo,
                    cashboxActions = selectedColor?.actions
                        ?.filter { it.isCashDesk == true }
                        ?.mapNotNull { it.name }
                        .orEmpty(),
                    wearWithProducts = selectedColor?.wearWith.orEmpty().mapNotNull { it.toRelatedItemEntity() },
                    completeSetProducts = selectedColor?.kits.orEmpty().mapNotNull { it.toRelatedItemEntity() },
                    availableSizes = selectedColor?.availableSizes?.let { availableSizes ->
                        ProductAvailableSizesEntity(
                            items = availableSizes.items
                                ?.mapNotNull { size ->
                                    val sizeId = size.sizeId ?: return@mapNotNull null
                                    ProductAvailableSizeEntity(
                                        sizeId = sizeId,
                                        russianSize = size.russianSize,
                                        sizeFullName = size.sizeFullName,
                                        inStock = size.inStock == true
                                    )
                                }
                                .orEmpty(),
                            countryCode = availableSizes.countryCode,
                            sizeTableTitle = availableSizes.sizeTableTitle,
                            sizeTableUrl = availableSizes.sizeTableUrl
                        )
                    }
                )
                productDao.upsert(entity)
                val relatedEntities = (entity.wearWithProducts + entity.completeSetProducts)
                    .mapIndexed { index, item ->
                        CatalogFilterProductsEntity(
                            categoryId = 0,
                            titleCategoryId = 0,
                            id = item.id,
                            itemId = item.itemId,
                            colorId = item.colorId,
                            name = item.name.orEmpty(),
                            price = item.price,
                            priceWithoutDiscount = item.priceWithoutDiscount,
                            brand = item.brand.orEmpty(),
                            urlBrandLogo = item.urlBrandLogo,
                            imageUrl = item.imageUrl.orEmpty(),
                            imageUrls = item.imageUrls,
                            additionalColorPhotoUrls = emptyList(),
                            position = index
                        )
                    }
                if (relatedEntities.isNotEmpty()) {
                    catalogFilterProductsDao.upsert(relatedEntities)
                }
            }
        )
    }
}

private fun CatalogProductSearchCardV2Response.toRelatedItemEntity(): ProductRelatedItemEntity? {
    val id = id ?: return null
    val itemId = itemId ?: return null
    val colorId = colorId ?: return null
    return ProductRelatedItemEntity(
        id = id,
        itemId = itemId,
        colorId = colorId,
        name = name,
        brand = brand,
        urlBrandLogo = urlBrandLogo,
        price = price ?: 0.0,
        priceWithoutDiscount = priceWithoutDiscount,
        imageUrl = imageUrl,
        imageUrls = imageUrls.orEmpty()
    )
}
