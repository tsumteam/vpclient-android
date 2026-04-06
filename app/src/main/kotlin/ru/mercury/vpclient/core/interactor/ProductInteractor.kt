package ru.mercury.vpclient.core.interactor

import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.core.persistence.database.entity.ProductEntity

interface ProductInteractor {

    fun productFlow(id: String): Flow<ProductEntity>

    suspend fun loadProduct(id: String)
}
