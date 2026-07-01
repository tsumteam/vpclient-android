@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.persistence.database.dao.ProductDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductEntity
import javax.inject.Inject

// fixme
class ProductFlowUseCase @Inject constructor(
    private val productDao: ProductDao,
    dispatchers: SharedDispatchers
): FlowUseCase<String, ProductEntity>(dispatchers.io) {

    override fun execute(id: String): Flow<ProductEntity> {
        return productDao.selectFlow(id).map { it ?: ProductEntity.Empty }
    }
}
