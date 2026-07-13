@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.FilterValuesRequestData
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.request.CatalogFilterRequest
import ru.mercury.vpclient.shared.data.network.request.FilterValuesRequest
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogCategoryDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogFilterDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.FilterValuesDao
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import ru.mercury.vpclient.shared.domain.mapper.requests
import ru.mercury.vpclient.shared.domain.mapper.toFilterValuesEntity
import ru.mercury.vpclient.shared.domain.mapper.toFilterValuesPickers
import ru.mercury.vpclient.shared.domain.mapper.viewType
import javax.inject.Inject

// fixme
class CatalogFilterValuesUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val catalogCategoryDao: CatalogCategoryDao,
    private val catalogFilterDao: CatalogFilterDao,
    private val filterValuesDao: FilterValuesDao,
    dispatchers: SharedDispatchers
): UseCase<FilterValuesRequestData, Unit>(dispatchers.io) {

    override suspend fun execute(data: FilterValuesRequestData) {
        val categoryId = data.categoryId
        val titleCategoryId = data.titleCategoryId
        val chipId = data.chipId
        val existingPicker = catalogFilterDao.select(categoryId, titleCategoryId)
            ?.toFilterValuesPickers()
            ?.firstOrNull { picker -> picker.chipId == chipId }
        val requestFilters = data.selectedFilterValueChipIds.requests(
            categoryId = categoryId,
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
                val viewType = data.viewTypeOverride ?: catalogCategoryDao.selectNotNull(categoryId)
                    .viewType(categoryId, titleCategoryId)
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
            onSuccess = { response ->
                val filterValuesEntity = response.filterValues.orEmpty().toFilterValuesEntity(
                    chipId = chipId,
                    title = existingPicker?.title ?: chipId.substringAfter("_", chipId),
                    valueType = existingPicker?.valueType,
                    showSearchBar = existingPicker?.showSearchBar == true,
                    showSidePanelWithLetters = existingPicker?.showSidePanelWithLetters == true
                )
                filterValuesDao.upsert(filterValuesEntity)
            },
            onFailure = { error -> throw CatalogFilterValuesException(error.message) }
        )
    }

    data class CatalogFilterValuesException(
        override val message: String
    ): ClientException(message)

    data class FiltersNotSupportedException(
        override val message: String = ""
    ): ClientException(message)
}
