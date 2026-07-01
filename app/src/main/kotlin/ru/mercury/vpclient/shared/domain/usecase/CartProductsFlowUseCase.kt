package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.persistence.database.dao.CartProductDao
import ru.mercury.vpclient.shared.domain.mapper.cartProduct
import javax.inject.Inject

// fixme
class CartProductsFlowUseCase @Inject constructor(
    private val cartProductDao: CartProductDao,
    dispatchers: SharedDispatchers
): FlowUseCase<Unit, List<CartProduct>>(dispatchers.io) {

    override fun execute(parameters: Unit): Flow<List<CartProduct>> {
        return cartProductDao.selectAllFlow().map { entities ->
            entities.map { entity -> entity.cartProduct }
        }
    }
}
