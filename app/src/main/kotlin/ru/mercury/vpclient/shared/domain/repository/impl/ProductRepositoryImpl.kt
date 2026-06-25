@file:OptIn(ExperimentalPagingApi::class)

package ru.mercury.vpclient.shared.domain.repository.impl

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.mercury.vpclient.shared.data.PREFIX_SPACE
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.request.DetailCardRequest
import ru.mercury.vpclient.shared.data.persistence.database.AppDatabase
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogFilterProductsDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogViewHistoryProductDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.PagingKeyDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.ProductDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductAvailableSizeEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductAvailableSizesEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductButtonEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductOtherColorEntity
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import ru.mercury.vpclient.shared.domain.mapper.toRelatedItemEntity
import ru.mercury.vpclient.shared.domain.paging.CatalogViewHistoryRemoteMediator
import ru.mercury.vpclient.shared.domain.repository.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase,
    private val networkService: NetworkService,
    private val catalogFilterProductsDao: CatalogFilterProductsDao,
    private val catalogViewHistoryProductDao: CatalogViewHistoryProductDao,
    private val pagingKeyDao: PagingKeyDao,
    private val productDao: ProductDao
): ProductRepository {

    override fun productFlow(id: String): Flow<ProductEntity> {
        return productDao.selectFlow(id).map { it ?: ProductEntity.Empty }
    }

    override fun viewHistoryProductsFlow(): Flow<List<CatalogFilterProductsEntity>> {
        return catalogViewHistoryProductDao.selectFlow()
    }

    override fun viewHistoryProductsPagingData(): Flow<PagingData<CatalogFilterProductsEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = CATALOG_VIEW_HISTORY_PAGE_SIZE,
                initialLoadSize = CATALOG_VIEW_HISTORY_PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = CatalogViewHistoryRemoteMediator(
                networkService = networkService,
                appDatabase = appDatabase,
                catalogViewHistoryProductDao = catalogViewHistoryProductDao,
                pagingKeyDao = pagingKeyDao
            ),
            pagingSourceFactory = {
                catalogViewHistoryProductDao.pagingSource()
            }
        ).flow
    }

    override suspend fun loadProduct(id: String) {
        val request = catalogFilterProductsDao.select(id)?.let { catalogEntity ->
            DetailCardRequest(
                itemId = catalogEntity.itemId,
                colorId = catalogEntity.colorId
            )
        } ?: catalogViewHistoryProductDao.select(id)?.let { viewHistoryEntity ->
            DetailCardRequest(
                itemId = viewHistoryEntity.itemId,
                colorId = viewHistoryEntity.colorId
            )
        } ?: return

        handleResponse(
            request = {
                networkService.catalogDetailedProduct(request)
            },
            onSuccess = { response ->
                val selectedColor = response.colors?.let { colors ->
                    colors.find { it.colorId == request.colorId }
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
                        .joinToString(separator = PREFIX_SPACE)
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
                    buttons = response.buttons.orEmpty().mapNotNull { button ->
                        val title = button.title?.trim()?.takeIf { it.isNotEmpty() } ?: return@mapNotNull null
                        ProductButtonEntity(
                            title = title,
                            catalogLink = button.catalogLink
                        )
                    },
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
                    hasWearWith = selectedColor?.hasWearWith ?: selectedColor?.wearWith.isNullOrEmpty().not(),
                    wearWithButtonEnabled = selectedColor?.wearWithButtonEnabled ?: selectedColor?.wearWith.isNullOrEmpty().not(),
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
                            additionalColorPhotoUrls = item.additionalColorPhotoUrls,
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

private const val CATALOG_VIEW_HISTORY_PAGE_SIZE = 15
