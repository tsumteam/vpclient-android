@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import androidx.room.withTransaction
import kotlinx.serialization.json.buildJsonArray
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.CatalogFilterRequestData2
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.NetworkService
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
): UseCase<CatalogFilterRequestData2, Unit>(dispatchers.io) {

    override suspend fun execute(data: CatalogFilterRequestData2) {
        val categoryId = data.categoryId
        val titleCategoryId = data.titleCategoryId
        val categoryEntity = catalogCategoryDao.selectNotNull(categoryId)

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
                val filtersJson = buildJsonArray {
                    response.filters.orEmpty().forEach { filterJsonElement ->
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
    }

    data class CatalogFiltersException(
        override val message: String
    ): ClientException(message)
}
