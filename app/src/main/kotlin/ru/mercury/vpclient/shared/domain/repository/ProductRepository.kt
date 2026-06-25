package ru.mercury.vpclient.shared.domain.repository

import kotlinx.coroutines.flow.Flow
import androidx.paging.PagingData
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductEntity

interface ProductRepository {

    fun productFlow(id: String): Flow<ProductEntity>

    fun viewHistoryProductsFlow(): Flow<List<CatalogFilterProductsEntity>>

    fun viewHistoryProductsPagingData(): Flow<PagingData<CatalogFilterProductsEntity>>

    suspend fun loadProduct(id: String)
}
