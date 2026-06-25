@file:OptIn(ExperimentalPagingApi::class)

package ru.mercury.vpclient.shared.domain.repository.impl

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import ru.mercury.vpclient.shared.data.DEFAULT_PAGE_SIZE
import ru.mercury.vpclient.shared.data.entity.CatalogFilterProductsData
import ru.mercury.vpclient.shared.data.entity.CatalogFilterRequestData2
import ru.mercury.vpclient.shared.data.entity.FilterValuesRequestData
import ru.mercury.vpclient.shared.data.error.FiltersNotSupportedException
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.request.CatalogBrandFavoriteRequest
import ru.mercury.vpclient.shared.data.network.request.CatalogFilterRequest
import ru.mercury.vpclient.shared.data.network.request.FilterValuesRequest
import ru.mercury.vpclient.shared.data.network.request.FilteredProductsQuantityRequest
import ru.mercury.vpclient.shared.data.persistence.database.AppDatabase
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogCategoryDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogFilterDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogFilterProductsDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.FilterValuesDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.FilterValuesQuantityDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.PagingKeyDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesQuantityEntity
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult
import ru.mercury.vpclient.shared.domain.mapper.requests
import ru.mercury.vpclient.shared.domain.mapper.toFilterValuesEntity
import ru.mercury.vpclient.shared.domain.mapper.toFilterValuesPickers
import ru.mercury.vpclient.shared.domain.mapper.viewType
import ru.mercury.vpclient.shared.domain.paging.CatalogFilterProductsRemoteMediator
import ru.mercury.vpclient.shared.domain.repository.FilterRepository
import javax.inject.Inject

class FilterRepositoryImpl @Inject constructor(
    private val networkService: NetworkService,
    private val appDatabase: AppDatabase,
    private val catalogCategoryDao: CatalogCategoryDao,
    private val catalogFilterDao: CatalogFilterDao,
    private val catalogFilterProductsDao: CatalogFilterProductsDao,
    private val filterValuesDao: FilterValuesDao,
    private val filterValuesQuantityDao: FilterValuesQuantityDao,
    private val pagingKeyDao: PagingKeyDao
): FilterRepository {

    override fun filterProductsPagingData(data: CatalogFilterProductsData): Flow<PagingData<CatalogFilterProductsEntity>> {
        val categoryId = data.categoryId
        val titleCategoryId = data.titleCategoryId

        return Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE,
                enablePlaceholders = true
            ),
            remoteMediator = CatalogFilterProductsRemoteMediator(
                data = data,
                networkService = networkService,
                appDatabase = appDatabase,
                catalogCategoryDao = catalogCategoryDao,
                catalogFilterProductsDao = catalogFilterProductsDao,
                pagingKeyDao = pagingKeyDao
            ),
            pagingSourceFactory = {
                catalogFilterProductsDao.pagingSource(categoryId, titleCategoryId)
            }
        ).flow
    }

    override fun catalogFilterProductFlow(id: String): Flow<CatalogFilterProductsEntity> {
        return catalogFilterProductsDao.selectFlow(id)
    }

    override fun filterValuesEntityFlow(chipId: String): Flow<FilterValuesEntity> {
        return filterValuesDao.selectFlow(chipId).mapNotNull { it }
    }

    override fun filterValuesQuantityEntityFlow(chipId: String): Flow<FilterValuesQuantityEntity> {
        return filterValuesQuantityDao.selectFlow(chipId).mapNotNull { it }
    }

    override suspend fun loadCatalogFilterValues(data: FilterValuesRequestData) {
        val categoryId = data.categoryId
        val titleCategoryId = data.titleCategoryId
        val chipId = data.chipId
        val categoryEntity = catalogCategoryDao.selectNotNull(categoryId)
        val existingPicker = catalogFilterDao.select(categoryId, titleCategoryId)
            ?.toFilterValuesPickers()
            ?.firstOrNull { picker -> picker.chipId == chipId }
        val requestFilters = data.selectedFilterValueChipIds.requests(
            categoryId = categoryEntity.id,
            includeDefaultCategory = data.includeDefaultCategory
        )
        val filterType = chipId.substringBefore("_")
        val filterSubtype = chipId.substringAfter("_", "").ifBlank { null }
        val filterTypeDto = when (filterType) {
            CatalogFilterRequest.ACTION,
            CatalogFilterRequest.ATTRIBUTE,
            CatalogFilterRequest.BRAND,
            CatalogFilterRequest.CATEGORY,
            CatalogFilterRequest.COLOR,
            CatalogFilterRequest.PRICE,
            CatalogFilterRequest.SIZE -> filterType
            else -> throw FiltersNotSupportedException()
        }

        handleResponse(
            request = {
                val viewType = data.viewTypeOverride ?: categoryEntity.viewType(categoryId, titleCategoryId)
                val request = FilterValuesRequest(
                    filterType = filterTypeDto,
                    filterSubtype = filterSubtype,
                    filterTreeValuesLevel = 0,
                    viewType = viewType,
                    hasUserInteractedWithStandartSizesFilter = false,
                    filters = requestFilters
                )
                networkService.catalogFilterValues(request)
            },
            onSuccess = { data ->
                val filterValuesEntity = data.filterValues.orEmpty().toFilterValuesEntity(
                    chipId = chipId,
                    title = existingPicker?.title ?: chipId.substringAfter("_", chipId),
                    valueType = existingPicker?.valueType,
                    showSearchBar = existingPicker?.showSearchBar == true,
                    showSidePanelWithLetters = existingPicker?.showSidePanelWithLetters == true
                )
                filterValuesDao.upsert(filterValuesEntity)
            }
        )
    }

    override suspend fun loadCatalogFiltersProductsQuantity(chipId: String, data: CatalogFilterRequestData2) {
        val categoryId = data.categoryId
        val titleCategoryId = data.titleCategoryId
        val categoryEntity = catalogCategoryDao.selectNotNull(categoryId)

        handleResponse(
            request = {
                val viewType = data.viewTypeOverride ?: categoryEntity.viewType(categoryId, titleCategoryId)
                val request = FilteredProductsQuantityRequest(
                    viewType = viewType,
                    hasUserInteractedWithStandartSizesFilter = false,
                    filters = data.selectedFilterValueChipIds.requests(
                        categoryId = categoryEntity.id,
                        includeDefaultCategory = data.includeDefaultCategory
                    )
                )
                networkService.catalogFilterProductsQuantity(request)
            },
            onSuccess = { response ->
                val filterValuesQuantityEntity = FilterValuesQuantityEntity(
                    chipId = chipId,
                    quantity = response.quantity
                )
                filterValuesQuantityDao.upsert(filterValuesQuantityEntity)
            }
        )
    }

    override suspend fun resetFilterValuesQuantity(chipId: String) {
        val entity = FilterValuesQuantityEntity(chipId = chipId, quantity = null)
        filterValuesQuantityDao.upsert(entity)
    }

    override suspend fun toggleBrandFavorite(chipId: String, brandId: Int, categoryId: Int, isFavorite: Boolean) {
        val pickerChipId = chipId.substringBefore("_")
        val entity = filterValuesDao.select(pickerChipId)
        val updatedEntity = entity?.copy(
            items = entity.items.map { item ->
                if (item.requestValue == brandId.toString()) item.copy(isFavorite = isFavorite) else item
            }
        )
        if (updatedEntity != null) {
            filterValuesDao.upsert(updatedEntity)
        }
        handleResponse(
            request = {
                val request = CatalogBrandFavoriteRequest(brandId = brandId, categoryId = categoryId)
                if (isFavorite) networkService.catalogBrandsLike(request)
                else networkService.catalogBrandsUnlike(request)
            },
            onFailure = {
                if (entity != null) {
                    filterValuesDao.upsert(entity)
                }
            }
        )
    }

    override suspend fun loadBrandFavoriteStatus(brandId: Int, categoryId: Int): Boolean {
        return handleResponseResult {
            networkService.catalogBrandsIsFavorite(brandId, categoryId)
        }.getOrDefault(false)
    }
}
