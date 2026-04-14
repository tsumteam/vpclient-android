package ru.mercury.vpclient.shared.repository

import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.persistence.database.entity.ProductEntity

interface ProductRepository {

    fun productFlow(id: String): Flow<ProductEntity>

    suspend fun loadProduct(id: String)
}
