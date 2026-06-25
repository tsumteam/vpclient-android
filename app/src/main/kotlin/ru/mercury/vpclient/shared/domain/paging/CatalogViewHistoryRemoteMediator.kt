@file:OptIn(ExperimentalPagingApi::class)

package ru.mercury.vpclient.shared.domain.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.persistence.database.AppDatabase
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogViewHistoryProductDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.PagingKeyDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.PagingKeyEntity
import ru.mercury.vpclient.shared.domain.mapper.entity
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult

class CatalogViewHistoryRemoteMediator(
    private val networkService: NetworkService,
    private val appDatabase: AppDatabase,
    private val catalogViewHistoryProductDao: CatalogViewHistoryProductDao,
    private val pagingKeyDao: PagingKeyDao
): RemoteMediator<Int, CatalogFilterProductsEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CatalogFilterProductsEntity>
    ): MediatorResult {
        return try {
            val pagingKeyEntity = pagingKeyDao.select(
                categoryId = CATALOG_VIEW_HISTORY_CATEGORY_ID,
                titleCategoryId = CATALOG_VIEW_HISTORY_TITLE_CATEGORY_ID
            )
            val paginationToken = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> pagingKeyEntity?.paginationToken
                    ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
            val loadLimit = when (loadType) {
                LoadType.REFRESH -> state.config.initialLoadSize
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> state.config.pageSize
            }
            val response = handleResponseResult {
                networkService.catalogViewHistory(
                    limit = loadLimit,
                    paginationToken = paginationToken
                )
            }.getOrThrow()
            val products = response.items.orEmpty()
            val nextPaginationToken = response.paginationToken?.takeIf { token -> token.isNotEmpty() }
            val isEndOfPaginationReached = products.size < loadLimit || nextPaginationToken == null
            val positionOffset = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> 0
                LoadType.APPEND -> state.pages.sumOf { page -> page.data.size }
            }

            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    pagingKeyDao.remove(
                        categoryId = CATALOG_VIEW_HISTORY_CATEGORY_ID,
                        titleCategoryId = CATALOG_VIEW_HISTORY_TITLE_CATEGORY_ID
                    )
                    catalogViewHistoryProductDao.delete()
                }
                pagingKeyDao.upsert(
                    PagingKeyEntity(
                        categoryId = CATALOG_VIEW_HISTORY_CATEGORY_ID,
                        titleCategoryId = CATALOG_VIEW_HISTORY_TITLE_CATEGORY_ID,
                        offset = null,
                        limit = state.config.pageSize,
                        paginationToken = nextPaginationToken.takeUnless { isEndOfPaginationReached }
                    )
                )
                catalogViewHistoryProductDao.upsert(
                    products.mapIndexed { index, product ->
                        product.entity(
                            position = positionOffset + index
                        )
                    }
                )
            }

            MediatorResult.Success(endOfPaginationReached = isEndOfPaginationReached)
        } catch (throwable: Throwable) {
            MediatorResult.Error(throwable)
        }
    }

    private companion object {
        private const val CATALOG_VIEW_HISTORY_CATEGORY_ID = -1
        private const val CATALOG_VIEW_HISTORY_TITLE_CATEGORY_ID = -1
    }
}
