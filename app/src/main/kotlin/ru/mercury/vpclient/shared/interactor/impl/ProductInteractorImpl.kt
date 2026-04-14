package ru.mercury.vpclient.shared.interactor.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.mercury.vpclient.shared.coroutines.ClientDispatchers
import ru.mercury.vpclient.shared.interactor.ProductInteractor
import ru.mercury.vpclient.shared.persistence.database.entity.ProductEntity
import ru.mercury.vpclient.shared.repository.ProductRepository
import javax.inject.Inject

class ProductInteractorImpl @Inject constructor(
    private val dispatchers: ClientDispatchers,
    private val productRepository: ProductRepository
): ProductInteractor {

    override fun productFlow(id: String): Flow<ProductEntity> {
        return productRepository.productFlow(id)
    }

    override suspend fun loadProduct(id: String) {
        withContext(dispatchers.io) { productRepository.loadProduct(id) }
    }
}
