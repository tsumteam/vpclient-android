package ru.mercury.vpclient.core.repository

import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.core.persistence.database.entity.ProductEntity

interface ProductRepository {

    fun productFlow(id: String): Flow<ProductEntity>

    suspend fun loadProduct(id: String)
}
