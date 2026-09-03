@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.FilterValuesRequestData
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.request.CatalogFilterRequest
import ru.mercury.vpclient.shared.data.network.request.DigineticaFilterValuesRequest
import ru.mercury.vpclient.shared.data.network.request.FilterValuesRequest
import ru.mercury.vpclient.shared.data.network.response.BaseResponse
import ru.mercury.vpclient.shared.data.network.response.FilterValuesResponse
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogCategoryDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogFilterDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.FilterValuesDao
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import ru.mercury.vpclient.shared.domain.mapper.isNetworkRelated
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
        val existingPicker = catalogFilterDao.select(categoryId, titleCategoryId, data.searchText)
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
                lateinit var result: BaseResponse<FilterValuesResponse>
                var attempt = 0
                while (true) {
                    try {
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
                        result = when {
                            data.searchText.isNotEmpty() -> {
                                val digineticaRequest = DigineticaFilterValuesRequest(
                                    searchText = data.searchText,
                                    request = request
                                )
                                networkService.catalogByTextFilterValues(digineticaRequest)
                            }
                            else -> networkService.catalogFilterValues(request)
                        }
                        break
                    } catch (throwable: CancellationException) {
                        throw throwable
                    } catch (throwable: Throwable) {
                        if (!throwable.isNetworkRelated || attempt >= MAX_RETRY_ATTEMPTS) throw throwable
                        val retryDelayMillis = when (attempt) {
                            0 -> FIRST_RETRY_DELAY_MILLIS
                            else -> SECOND_RETRY_DELAY_MILLIS
                        }
                        delay(retryDelayMillis)
                        attempt++
                    }
                }
                result
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

    private companion object {
        private const val MAX_RETRY_ATTEMPTS = 2
        private const val FIRST_RETRY_DELAY_MILLIS = 500L
        private const val SECOND_RETRY_DELAY_MILLIS = 1_500L
    }
}
