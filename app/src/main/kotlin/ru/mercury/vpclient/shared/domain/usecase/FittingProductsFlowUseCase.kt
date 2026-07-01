package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.persistence.database.dao.CartProductDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.CartProductEntity
import javax.inject.Inject

class FittingProductsFlowUseCase @Inject constructor(
    private val cartProductDao: CartProductDao,
    dispatchers: SharedDispatchers
): FlowUseCase<Unit, List<CartProductEntity>>(dispatchers.io) {

    override fun execute(parameters: Unit): Flow<List<CartProductEntity>> {
        return cartProductDao.selectAllFlow().map { entities ->
            entities.filter { product -> product.size.isNotEmpty() && !product.isSold }
        }
    }
}
