package ru.mercury.vpclient.shared.domain.interactor

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.data.entity.CatalogFilterProductsData
import ru.mercury.vpclient.shared.data.entity.CatalogFilterRequestData2
import ru.mercury.vpclient.shared.data.entity.FilterData
import ru.mercury.vpclient.shared.data.entity.FilterRequestData
import ru.mercury.vpclient.shared.data.entity.FilterValuesRequestData
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesQuantityEntity

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

    suspend fun toggleBrandFavorite(chipId: String, brandId: Int, categoryId: Int, isFavorite: Boolean)

    suspend fun loadBrandFavoriteStatus(brandId: Int, categoryId: Int): Boolean?
}
