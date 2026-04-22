@file:OptIn(ExperimentalPagingApi::class)

package ru.mercury.vpclient.shared.domain.repository.impl

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import ru.mercury.vpclient.shared.data.entity.CatalogFilterProductsData
import ru.mercury.vpclient.shared.domain.mapper.entity
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult
import ru.mercury.vpclient.shared.domain.mapper.requestValue
import ru.mercury.vpclient.shared.domain.mapper.requests
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.request.FilteredProductsRequest
import ru.mercury.vpclient.shared.data.network.response.FiltersResponse
import ru.mercury.vpclient.shared.data.persistence.database.AppDatabase
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogCategoryDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogFilterProductsDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.PagingKeyDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.PagingKeyEntity

class CatalogFilterProductsRemoteMediator(
    private val data: CatalogFilterProductsData,
    private val networkService: NetworkService,
    private val appDatabase: AppDatabase,
    private val catalogCategoryDao: CatalogCategoryDao,
    private val catalogFilterProductsDao: CatalogFilterProductsDao,
    private val pagingKeyDao: PagingKeyDao
): RemoteMediator<Int, CatalogFilterProductsEntity>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, CatalogFilterProductsEntity>): MediatorResult {
        return try {
            val categoryId = data.categoryId
            val titleCategoryId = data.titleCategoryId
            val pagingKeyEntity = pagingKeyDao.select(categoryId, titleCategoryId)
            val loadOffset = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> pagingKeyEntity?.offset ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
            val loadLimit = when (loadType) {
                LoadType.REFRESH -> state.config.initialLoadSize
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> pagingKeyEntity?.limit ?: state.config.pageSize
            }
            val categoryEntity = catalogCategoryDao.select(categoryId)
            val viewType = data.viewTypeOverride ?: when {
                categoryId == titleCategoryId -> FiltersResponse.VIEW_TYPE_CATALOG_LEVEL_5
                titleCategoryId == categoryEntity.rootId -> FiltersResponse.VIEW_TYPE_CATALOG_LEVEL_3
                else -> FiltersResponse.VIEW_TYPE_CATALOG_LEVEL_4
            }
            val response = handleResponseResult {
                val request = FilteredProductsRequest(
                    viewType = viewType,
                    sortType = data.sortType.requestValue,
                    hasUserInteractedWithStandartSizesFilter = false,
                    filters = data.selectedFilterValueChipIds.requests(
                        categoryId = categoryEntity.id,
                        includeDefaultCategory = data.includeDefaultCategory
                    )
                )
                networkService.catalogProducts(loadLimit, loadOffset, request)
            }
            val products = response.getOrThrow().items.orEmpty()
            val isEndOfPaginationReached = products.size < loadLimit

            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    pagingKeyDao.remove(categoryId, titleCategoryId)
                    catalogFilterProductsDao.remove(categoryId, titleCategoryId)
                }
                pagingKeyDao.upsert(
                    PagingKeyEntity(
                        categoryId = categoryId,
                        titleCategoryId = titleCategoryId,
                        offset = if (isEndOfPaginationReached) null else loadOffset + products.size,
                        limit = state.config.pageSize
                    )
                )
                catalogFilterProductsDao.upsert(
                    products.mapIndexed { index, product ->
                        product.entity(
                            categoryId = categoryId,
                            titleCategoryId = titleCategoryId,
                            position = loadOffset + index
                        )
                    }
                )
            }

            MediatorResult.Success(endOfPaginationReached = isEndOfPaginationReached)
        } catch (throwable: Throwable) {
            MediatorResult.Error(throwable)
        }
    }
}
