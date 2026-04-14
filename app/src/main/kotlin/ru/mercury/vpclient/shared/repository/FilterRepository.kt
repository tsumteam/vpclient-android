package ru.mercury.vpclient.shared.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.entity.CatalogFilterProductsData
import ru.mercury.vpclient.shared.entity.CatalogFilterRequestData2
import ru.mercury.vpclient.shared.entity.FilterData
import ru.mercury.vpclient.shared.entity.FilterRequestData
import ru.mercury.vpclient.shared.entity.FilterValuesRequestData
import ru.mercury.vpclient.shared.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.persistence.database.entity.FilterValuesEntity
import ru.mercury.vpclient.shared.persistence.database.entity.FilterValuesQuantityEntity

interface FilterRepository {

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
