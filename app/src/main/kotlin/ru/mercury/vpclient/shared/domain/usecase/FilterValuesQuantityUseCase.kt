package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.CatalogFilterRequestData2
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.request.FilteredProductsQuantityRequest
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogCategoryDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.FilterValuesQuantityDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesQuantityEntity
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import ru.mercury.vpclient.shared.domain.mapper.requests
import ru.mercury.vpclient.shared.domain.mapper.viewType
import javax.inject.Inject

// fixme
class FilterValuesQuantityUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val catalogCategoryDao: CatalogCategoryDao,
    private val filterValuesQuantityDao: FilterValuesQuantityDao,
    dispatchers: SharedDispatchers
): UseCase<FilterValuesQuantityUseCase.Params, Unit>(dispatchers.io) {

    override suspend fun execute(params: Params) {
        val data = params.data
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
                    chipId = params.chipId,
                    quantity = response.quantity
                )
                filterValuesQuantityDao.upsert(filterValuesQuantityEntity)
            },
            onFailure = { error -> throw FilterValuesQuantityException(error.message) }
        )
    }

    data class Params(
        val chipId: String,
        val data: CatalogFilterRequestData2
    )

    data class FilterValuesQuantityException(
        override val message: String
    ): ClientException(message)
}
