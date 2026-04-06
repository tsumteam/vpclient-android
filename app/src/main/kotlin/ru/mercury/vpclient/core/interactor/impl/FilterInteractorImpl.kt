package ru.mercury.vpclient.core.interactor.impl

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.mercury.vpclient.core.coroutines.ClientDispatchers
import ru.mercury.vpclient.core.entity.CatalogFilterProductsData
import ru.mercury.vpclient.core.entity.CatalogFilterRequestData2
import ru.mercury.vpclient.core.entity.FilterData
import ru.mercury.vpclient.core.entity.FilterRequestData
import ru.mercury.vpclient.core.entity.FilterValuesRequestData
import ru.mercury.vpclient.core.interactor.FilterInteractor
import ru.mercury.vpclient.core.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.core.persistence.database.entity.FilterValuesEntity
import ru.mercury.vpclient.core.persistence.database.entity.FilterValuesQuantityEntity
import ru.mercury.vpclient.core.repository.FilterRepository
import javax.inject.Inject

class FilterInteractorImpl @Inject constructor(
    private val dispatchers: ClientDispatchers,
    private val filterRepository: FilterRepository
): FilterInteractor {

    override fun filterDataFlow(data: FilterRequestData): Flow<FilterData> {
        return filterRepository.filterDataFlow(data)
    }

    override fun filterProductsPagingData(data: CatalogFilterProductsData): Flow<PagingData<CatalogFilterProductsEntity>> {
        return filterRepository.filterProductsPagingData(data)
    }

    override fun catalogFilterProductFlow(id: String): Flow<CatalogFilterProductsEntity> {
        return filterRepository.catalogFilterProductFlow(id)
    }

    override fun filterValuesEntityFlow(chipId: String): Flow<FilterValuesEntity> {
        return filterRepository.filterValuesEntityFlow(chipId)
    }

    override fun filterValuesQuantityEntityFlow(chipId: String): Flow<FilterValuesQuantityEntity> {
        return filterRepository.filterValuesQuantityEntityFlow(chipId)
    }

    override suspend fun loadCatalogFilters(data: CatalogFilterRequestData2) {
        return withContext(dispatchers.io) { filterRepository.loadCatalogFilters(data) }
    }

    override suspend fun loadCatalogFilterValues(data: FilterValuesRequestData) {
        withContext(dispatchers.io) { filterRepository.loadCatalogFilterValues(data) }
    }

    override suspend fun loadCatalogFiltersProductsQuantity(data: CatalogFilterRequestData2) {
        return withContext(dispatchers.io) { filterRepository.loadCatalogFiltersProductsQuantity(data) }
    }

    override suspend fun loadCatalogFiltersProductsQuantity(chipId: String, data: CatalogFilterRequestData2) {
        withContext(dispatchers.io) { filterRepository.loadCatalogFiltersProductsQuantity(chipId, data) }
    }

    override suspend fun resetFilterValuesQuantity(chipId: String) {
        withContext(dispatchers.io) { filterRepository.resetFilterValuesQuantity(chipId) }
    }
}
