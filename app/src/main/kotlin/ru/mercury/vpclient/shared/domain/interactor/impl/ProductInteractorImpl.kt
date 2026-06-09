package ru.mercury.vpclient.shared.domain.interactor.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductEntity
import ru.mercury.vpclient.shared.domain.interactor.ProductInteractor
import ru.mercury.vpclient.shared.domain.repository.ProductRepository
import javax.inject.Inject

class ProductInteractorImpl @Inject constructor(
    private val dispatchers: SharedDispatchers,
    private val productRepository: ProductRepository
): ProductInteractor {

    override fun productFlow(id: String): Flow<ProductEntity> {
        return productRepository.productFlow(id)
    }

    override fun viewHistoryProductsFlow(): Flow<List<CatalogFilterProductsEntity>> {
        return productRepository.viewHistoryProductsFlow()
    }

    override suspend fun loadProduct(id: String) {
        withContext(dispatchers.io) { productRepository.loadProduct(id) }
    }

    override suspend fun loadViewHistoryProducts(limit: Int) {
        withContext(dispatchers.io) { productRepository.loadViewHistoryProducts(limit) }
    }
}
