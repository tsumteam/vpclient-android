@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import androidx.room.withTransaction
import kotlinx.serialization.json.buildJsonArray
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.CatalogFilterRequestData2
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.request.FiltersRequest
import ru.mercury.vpclient.shared.data.persistence.database.AppDatabase
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogCategoryDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogFilterDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterEntity
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import ru.mercury.vpclient.shared.domain.mapper.requests
import ru.mercury.vpclient.shared.domain.mapper.viewType
import javax.inject.Inject

class CatalogFiltersUseCase @Inject constructor(
    private val appDatabase: AppDatabase,
    private val networkService: NetworkService,
    private val catalogCategoryDao: CatalogCategoryDao,
    private val catalogFilterDao: CatalogFilterDao,
    dispatchers: SharedDispatchers
): UseCase<CatalogFilterRequestData2, Boolean>(dispatchers.io) {

    override suspend fun execute(data: CatalogFilterRequestData2): Boolean {
        val categoryId = data.categoryId
        val titleCategoryId = data.titleCategoryId
        var hasFilters = false

        handleResponse(
            request = {
                val viewType = data.viewTypeOverride ?: catalogCategoryDao.selectNotNull(categoryId)
                    .viewType(categoryId, titleCategoryId)
                val request = FiltersRequest(
                    viewType = viewType,
                    hasUserInteractedWithStandartSizesFilter = false,
                    filters = data.selectedFilterValueChipIds.requests(
                        categoryId = categoryId,
                        includeDefaultCategory = data.includeDefaultCategory
                    )
                )
                networkService.catalogFilters(request)
            },
            onSuccess = { response ->
                val filters = response.filters.orEmpty()
                hasFilters = filters.isNotEmpty()
                val filtersJson = buildJsonArray {
                    filters.forEach { filterJsonElement ->
                        add(filterJsonElement)
                    }
                }.toString()
                val catalogFilterEntity = CatalogFilterEntity(
                    categoryId = categoryId,
                    titleCategoryId = titleCategoryId,
                    filtersJson = filtersJson
                )
                appDatabase.withTransaction {
                    catalogFilterDao.delete(categoryId, titleCategoryId)
                    catalogFilterDao.upsert(catalogFilterEntity)
                }
            },
            onFailure = { error -> throw CatalogFiltersException(error.message) }
        )
        return hasFilters
    }

    data class CatalogFiltersException(
        override val message: String
    ): ClientException(message)
}
