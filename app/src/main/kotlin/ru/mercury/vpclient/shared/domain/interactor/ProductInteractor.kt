package ru.mercury.vpclient.shared.domain.interactor

import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductEntity

interface ProductInteractor {

    fun productFlow(id: String): Flow<ProductEntity>

    fun viewHistoryProductsFlow(): Flow<List<CatalogFilterProductsEntity>>

    suspend fun loadProduct(id: String)

    suspend fun loadViewHistoryProducts(limit: Int)
}
