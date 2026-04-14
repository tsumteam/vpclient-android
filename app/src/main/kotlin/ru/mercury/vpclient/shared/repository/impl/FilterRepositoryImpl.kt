@file:OptIn(ExperimentalPagingApi::class)

package ru.mercury.vpclient.shared.repository.impl

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.json.buildJsonArray
import ru.mercury.vpclient.shared.DEFAULT_PAGE_SIZE
import ru.mercury.vpclient.shared.entity.CatalogFilterProductsData
import ru.mercury.vpclient.shared.entity.CatalogFilterRequestData2
import ru.mercury.vpclient.shared.entity.FilterData
import ru.mercury.vpclient.shared.entity.FilterRequestData
import ru.mercury.vpclient.shared.entity.FilterRibbonData
import ru.mercury.vpclient.shared.entity.FilterTitleEntity
import ru.mercury.vpclient.shared.entity.FilterValuesRequestData
import ru.mercury.vpclient.shared.exception.FiltersNotSupportedException
import ru.mercury.vpclient.shared.ktx.handleResponse
import ru.mercury.vpclient.shared.ktx.orEmpty
import ru.mercury.vpclient.shared.ktx.requests
import ru.mercury.vpclient.shared.ktx.toFilterRibbonData
import ru.mercury.vpclient.shared.ktx.toFilterValuesEntity
import ru.mercury.vpclient.shared.ktx.toFilterValuesPickers
import ru.mercury.vpclient.shared.ktx.viewType
import ru.mercury.vpclient.shared.network.NetworkService
import ru.mercury.vpclient.shared.network.request.CatalogFilterRequest
import ru.mercury.vpclient.shared.network.request.FilterValuesRequest
import ru.mercury.vpclient.shared.network.request.FilteredProductsQuantityRequest
import ru.mercury.vpclient.shared.network.request.FiltersRequest
import ru.mercury.vpclient.shared.persistence.database.AppDatabase
import ru.mercury.vpclient.shared.persistence.database.dao.CatalogCategoryDao
import ru.mercury.vpclient.shared.persistence.database.dao.CatalogFilterDao
import ru.mercury.vpclient.shared.persistence.database.dao.CatalogFilterProductsDao
import ru.mercury.vpclient.shared.persistence.database.dao.CatalogFilterProductsQuantityDao
import ru.mercury.vpclient.shared.persistence.database.dao.FilterValuesDao
import ru.mercury.vpclient.shared.persistence.database.dao.FilterValuesQuantityDao
import ru.mercury.vpclient.shared.persistence.database.dao.PagingKeyDao
import ru.mercury.vpclient.shared.persistence.database.entity.CatalogFilterEntity
import ru.mercury.vpclient.shared.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.persistence.database.entity.CatalogFilterProductsQuantityEntity
import ru.mercury.vpclient.shared.persistence.database.entity.FilterValuesEntity
import ru.mercury.vpclient.shared.persistence.database.entity.FilterValuesQuantityEntity
import ru.mercury.vpclient.shared.repository.FilterRepository
import javax.inject.Inject

// fixme

class FilterRepositoryImpl @Inject constructor(
    private val networkService: NetworkService,
    private val appDatabase: AppDatabase,
    private val catalogCategoryDao: CatalogCategoryDao,
    private val catalogFilterDao: CatalogFilterDao,
    private val catalogFilterProductsDao: CatalogFilterProductsDao,
    private val catalogFilterProductsQuantityDao: CatalogFilterProductsQuantityDao,
    private val filterValuesDao: FilterValuesDao,
    private val filterValuesQuantityDao: FilterValuesQuantityDao,
    private val pagingKeyDao: PagingKeyDao
): FilterRepository {

    override fun filterDataFlow(data: FilterRequestData): Flow<FilterData> {
        val categoryId = data.categoryId
        val titleCategoryId = data.titleCategoryId
        val subtitleCategoryId = data.subtitleCategoryId
        val filterDataFlow = combine(
            catalogCategoryDao.selectFlow(titleCategoryId),
            catalogCategoryDao.selectFlow(subtitleCategoryId)
        ) { titleCatalogCategoryEntity, subtitleCatalogCategoryEntity ->
            FilterTitleEntity(titleCatalogCategoryEntity, subtitleCatalogCategoryEntity)
        }
        val filterRibbonDataFlow = catalogFilterDao.selectFlow(
            categoryId,
            titleCategoryId
        ).map { catalogFilterEntity ->
            when {
                catalogFilterEntity == null -> FilterRibbonData.Empty to emptyList()
                else -> catalogFilterEntity.toFilterRibbonData() to catalogFilterEntity.toFilterValuesPickers()
            }
        }
        val productsQuantityFlow = catalogFilterProductsQuantityDao.selectFlow(
            categoryId,
            titleCategoryId
        ).map { it.orEmpty }

        return combine(
            filterDataFlow,
            filterRibbonDataFlow,
            productsQuantityFlow
        ) { filterTitle, (filterRibbonData, filterValuesPickers), quantityEntity ->
            FilterData(
                filterTitleEntity = filterTitle,
                filterRibbonData = filterRibbonData,
                quantityEntity = quantityEntity,
                filterValuesEntities = filterValuesPickers
            )
        }
    }

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

    override suspend fun loadCatalogFilters(data: CatalogFilterRequestData2) {
        val categoryId = data.categoryId
        val titleCategoryId = data.titleCategoryId
        val categoryEntity = catalogCategoryDao.select(categoryId)

        handleResponse(
            request = {
                val request = FiltersRequest(
                    viewType = categoryEntity.viewType(categoryId, titleCategoryId),
                    hasUserInteractedWithStandartSizesFilter = false,
                    filters = data.selectedFilterValueChipIds.requests(categoryEntity.id)
                )
                networkService.catalogFilters(request)
            },
            onSuccess = { response ->
                val filtersJson = buildJsonArray { response.filters.orEmpty().forEach { filterJsonElement -> add(filterJsonElement) } }.toString()
                val catalogFilterEntity = CatalogFilterEntity(
                    categoryId = categoryId,
                    titleCategoryId = titleCategoryId,
                    filtersJson = filtersJson
                )
                catalogFilterDao.upsert(catalogFilterEntity)
            }
        )
    }

    override suspend fun loadCatalogFilterValues(data: FilterValuesRequestData) { // fixme
        val categoryId = data.categoryId
        val titleCategoryId = data.titleCategoryId
        val chipId = data.chipId
        val categoryEntity = catalogCategoryDao.select(categoryId)
        val requestFilters = data.selectedFilterValueChipIds.requests(categoryEntity.id)
        val filterType = chipId.substringBefore("_")
        val filterSubtype = chipId.substringAfter("_", "").ifBlank { null }
        val filterTypeDto = when (filterType) {
            CatalogFilterRequest.ACTION, CatalogFilterRequest.ATTRIBUTE, CatalogFilterRequest.BRAND, CatalogFilterRequest.COLOR, CatalogFilterRequest.SIZE -> filterType
            else -> throw FiltersNotSupportedException()
        }

        handleResponse(
            request = {
                val request = FilterValuesRequest(
                    filterType = filterTypeDto,
                    filterSubtype = filterSubtype,
                    filterTreeValuesLevel = 0,
                    viewType = categoryEntity.viewType(categoryId, titleCategoryId),
                    hasUserInteractedWithStandartSizesFilter = false,
                    filters = requestFilters
                )
                networkService.catalogFilterValues(request)
            },
            onSuccess = { data ->
                val filterValuesEntity = data.filterValues.orEmpty().toFilterValuesEntity(
                    chipId = chipId,
                    title = chipId.substringAfter("_", chipId)
                )
                filterValuesDao.upsert(filterValuesEntity)
            }
        )
    }

    override suspend fun loadCatalogFiltersProductsQuantity(data: CatalogFilterRequestData2) {
        val categoryId = data.categoryId
        val titleCategoryId = data.titleCategoryId
        val categoryEntity = catalogCategoryDao.select(categoryId)

        handleResponse(
            request = {
                val request = FilteredProductsQuantityRequest(
                    viewType = categoryEntity.viewType(categoryId, titleCategoryId),
                    hasUserInteractedWithStandartSizesFilter = false,
                    filters = data.selectedFilterValueChipIds.requests(categoryEntity.id)
                )
                networkService.catalogFilterProductsQuantity(request)
            },
            onSuccess = { response ->
                val catalogFilterProductsQuantityEntity = CatalogFilterProductsQuantityEntity(
                    categoryId = categoryId,
                    titleCategoryId = titleCategoryId,
                    productsQuantity = response.quantity
                )
                catalogFilterProductsQuantityDao.upsert(catalogFilterProductsQuantityEntity)
            }
        )
    }

    override suspend fun loadCatalogFiltersProductsQuantity(chipId: String, data: CatalogFilterRequestData2) {
        val categoryId = data.categoryId
        val titleCategoryId = data.titleCategoryId
        val categoryEntity = catalogCategoryDao.select(categoryId)

        handleResponse(
            request = {
                val request = FilteredProductsQuantityRequest(
                    viewType = categoryEntity.viewType(categoryId, titleCategoryId),
                    hasUserInteractedWithStandartSizesFilter = false,
                    filters = data.selectedFilterValueChipIds.requests(categoryEntity.id)
                )
                networkService.catalogFilterProductsQuantity(request)
            },
            onSuccess = { response ->
                val filterValuesQuantityEntity = FilterValuesQuantityEntity(chipId = chipId, quantity = response.quantity)
                filterValuesQuantityDao.upsert(filterValuesQuantityEntity)
            }
        )
    }

    override suspend fun resetFilterValuesQuantity(chipId: String) {
        val entity = FilterValuesQuantityEntity(chipId = chipId, quantity = null)
        filterValuesQuantityDao.upsert(entity)
    }
}
