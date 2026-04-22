@file:OptIn(ExperimentalPagingApi::class)

package ru.mercury.vpclient.shared.domain.repository.impl

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.json.buildJsonArray
import ru.mercury.vpclient.shared.data.DEFAULT_PAGE_SIZE
import ru.mercury.vpclient.shared.data.entity.CatalogFilterProductsData
import ru.mercury.vpclient.shared.data.entity.CatalogFilterRequestData2
import ru.mercury.vpclient.shared.data.entity.FilterData
import ru.mercury.vpclient.shared.data.entity.FilterRequestData
import ru.mercury.vpclient.shared.data.entity.FilterRibbonData
import ru.mercury.vpclient.shared.data.entity.FilterTitleEntity
import ru.mercury.vpclient.shared.data.entity.FilterValuesRequestData
import ru.mercury.vpclient.shared.data.error.FiltersNotSupportedException
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.request.CatalogFilterRequest
import ru.mercury.vpclient.shared.data.network.request.FilterValuesRequest
import ru.mercury.vpclient.shared.data.network.request.FilteredProductsQuantityRequest
import ru.mercury.vpclient.shared.data.network.request.FiltersRequest
import ru.mercury.vpclient.shared.data.persistence.database.AppDatabase
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogCategoryDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogFilterDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogFilterProductsDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogFilterProductsQuantityDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.FilterValuesDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.FilterValuesQuantityDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.PagingKeyDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsQuantityEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesQuantityEntity
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import ru.mercury.vpclient.shared.domain.mapper.orEmpty
import ru.mercury.vpclient.shared.domain.mapper.requests
import ru.mercury.vpclient.shared.domain.mapper.toFilterRibbonData
import ru.mercury.vpclient.shared.domain.mapper.toFilterValuesEntity
import ru.mercury.vpclient.shared.domain.mapper.toFilterValuesPickers
import ru.mercury.vpclient.shared.domain.mapper.viewType
import ru.mercury.vpclient.shared.domain.repository.FilterRepository
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
                val viewType = data.viewTypeOverride ?: categoryEntity.viewType(categoryId, titleCategoryId)
                val request = FiltersRequest(
                    viewType = viewType,
                    hasUserInteractedWithStandartSizesFilter = false,
                    filters = data.selectedFilterValueChipIds.requests(
                        categoryId = categoryEntity.id,
                        includeDefaultCategory = data.includeDefaultCategory
                    )
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

    override suspend fun loadCatalogFiltersProductsQuantity(data: CatalogFilterRequestData2) {
        val categoryId = data.categoryId
        val titleCategoryId = data.titleCategoryId
        val categoryEntity = catalogCategoryDao.select(categoryId)

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
}
