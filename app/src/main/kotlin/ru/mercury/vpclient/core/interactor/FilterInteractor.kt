package ru.mercury.vpclient.core.interactor

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.core.entity.CatalogFilterProductsData
import ru.mercury.vpclient.core.entity.CatalogFilterRequestData2
import ru.mercury.vpclient.core.entity.FilterData
import ru.mercury.vpclient.core.entity.FilterRequestData
import ru.mercury.vpclient.core.entity.FilterValuesRequestData
import ru.mercury.vpclient.core.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.core.persistence.database.entity.FilterValuesEntity
import ru.mercury.vpclient.core.persistence.database.entity.FilterValuesQuantityEntity

interface FilterInteractor {

    fun filterDataFlow(data: FilterRequestData): Flow<FilterData>

    fun filterProductsPagingData(data: CatalogFilterProductsData): Flow<PagingData<CatalogFilterProductsEntity>>

    fun catalogFilterProductFlow(id: String): Flow<CatalogFilterProductsEntity>

    fun filterValuesEntityFlow(chipId: String): Flow<FilterValuesEntity>

    fun filterValuesQuantityEntityFlow(chipId: String): Flow<FilterValuesQuantityEntity>

    suspend fun loadCatalogFilters(data: CatalogFilterRequestData2)

    suspend fun loadCatalogFilterValues(data: FilterValuesRequestData)

    suspend fun loadCatalogFiltersProductsQuantity(data: CatalogFilterRequestData2)

    suspend fun loadCatalogFiltersProductsQuantity(chipId: String, data: CatalogFilterRequestData2)

    suspend fun resetFilterValuesQuantity(chipId: String)
}
