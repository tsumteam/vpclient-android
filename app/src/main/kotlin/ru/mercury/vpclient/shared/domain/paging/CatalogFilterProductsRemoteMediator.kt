@file:OptIn(ExperimentalPagingApi::class)

package ru.mercury.vpclient.shared.domain.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import ru.mercury.vpclient.shared.data.entity.CatalogFilterProductsData
import ru.mercury.vpclient.shared.data.DEFAULT_PAGE_SIZE
import ru.mercury.vpclient.shared.data.entity.SearchResultsMetadata
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.request.DigineticaFilteredProductsRequest
import ru.mercury.vpclient.shared.data.network.request.FilteredProductsRequest
import ru.mercury.vpclient.shared.data.network.type.CatalogViewType
import ru.mercury.vpclient.shared.data.persistence.database.AppDatabase
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogCategoryDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogFilterProductsDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.PagingKeyDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.PagingKeyEntity
import ru.mercury.vpclient.shared.domain.mapper.entity
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult
import ru.mercury.vpclient.shared.domain.mapper.requestValue
import ru.mercury.vpclient.shared.domain.mapper.requests
import ru.mercury.vpclient.shared.domain.usecase.SetSearchResultsMetadataUseCase
import ru.mercury.vpclient.shared.domain.usecase.DigineticaSearchEventUseCase
import ru.mercury.vpclient.shared.domain.usecase.DigineticaSearchEventUseCase.Params

class CatalogFilterProductsRemoteMediator(
    private val data: CatalogFilterProductsData,
    private val networkService: NetworkService,
    private val appDatabase: AppDatabase,
    private val catalogCategoryDao: CatalogCategoryDao,
    private val catalogFilterProductsDao: CatalogFilterProductsDao,
    private val pagingKeyDao: PagingKeyDao,
    private val setSearchResultsMetadataUseCase: SetSearchResultsMetadataUseCase,
    private val digineticaSearchEventUseCase: DigineticaSearchEventUseCase
): RemoteMediator<Int, CatalogFilterProductsEntity>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, CatalogFilterProductsEntity>): MediatorResult {
        return try {
            val categoryId = data.categoryId
            val titleCategoryId = data.titleCategoryId
            val searchText = data.searchText
            val pagingKeyEntity = pagingKeyDao.select(categoryId, titleCategoryId, searchText)
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
            val viewType = data.viewTypeOverride ?: catalogCategoryDao.selectNotNull(categoryId).let { categoryEntity ->
                when {
                    categoryId == titleCategoryId -> CatalogViewType.CATALOG_LEVEL_5
                    titleCategoryId == categoryEntity.rootId -> CatalogViewType.CATALOG_LEVEL_3
                    else -> CatalogViewType.CATALOG_LEVEL_4
                }
            }
            val products = when {
                searchText.isNotEmpty() -> {
                    val response = handleResponseResult {
                        val filteredProductsRequest = FilteredProductsRequest(
                            viewType = viewType,
                            sortType = data.sortType.requestValue,
                            hasUserInteractedWithStandartSizesFilter = false,
                            filters = data.selectedFilterValueChipIds.requests(
                                categoryId = categoryId,
                                includeDefaultCategory = data.includeDefaultCategory
                            )
                        )
                        val request = DigineticaFilteredProductsRequest(
                            searchText = searchText,
                            filteredProductsRequest = filteredProductsRequest
                        )
                        networkService.catalogByTextProductsDiginetica(loadLimit, loadOffset, request)
                    }.getOrThrow()
                    setSearchResultsMetadataUseCase(
                        SearchResultsMetadata(
                            categoryId = categoryId,
                            titleCategoryId = titleCategoryId,
                            searchText = searchText,
                            searchRequestId = data.searchRequestId,
                            correction = response.correction,
                            catalogLink = response.catalogLink
                        )
                    ).getOrThrow()
                    val items = response.products?.items.orEmpty()
                    if (response.catalogLink == null) {
                        items.chunked(DEFAULT_PAGE_SIZE)
                            .ifEmpty { listOf(emptyList()) }
                            .forEachIndexed { index, pageItems ->
                            runCatching {
                                digineticaSearchEventUseCase(
                                    Params(
                                        pageNumber = (loadOffset / DEFAULT_PAGE_SIZE) + index,
                                        pageProducts = pageItems.mapNotNull { product ->
                                            product.id?.takeIf(String::isNotEmpty)
                                        },
                                        searchTerm = searchText
                                    )
                                ).getOrThrow()
                            }
                        }
                    }
                    items
                }
                else -> {
                    handleResponseResult {
                        val request = FilteredProductsRequest(
                            viewType = viewType,
                            sortType = data.sortType.requestValue,
                            hasUserInteractedWithStandartSizesFilter = false,
                            filters = data.selectedFilterValueChipIds.requests(
                                categoryId = categoryId,
                                includeDefaultCategory = data.includeDefaultCategory
                            )
                        )
                        networkService.catalogProducts(loadLimit, loadOffset, request)
                    }.getOrThrow().items.orEmpty()
                }
            }
            val isEndOfPaginationReached = products.size < loadLimit

            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    pagingKeyDao.remove(categoryId, titleCategoryId, searchText)
                    catalogFilterProductsDao.remove(categoryId, titleCategoryId, searchText)
                }
                pagingKeyDao.upsert(
                    PagingKeyEntity(
                        categoryId = categoryId,
                        titleCategoryId = titleCategoryId,
                        offset = if (isEndOfPaginationReached) null else loadOffset + products.size,
                        limit = state.config.pageSize,
                        paginationToken = null,
                        searchText = searchText
                    )
                )
                catalogFilterProductsDao.upsert(
                    products.mapIndexed { index, product ->
                        product.entity(
                            categoryId = categoryId,
                            titleCategoryId = titleCategoryId,
                            searchText = searchText,
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
