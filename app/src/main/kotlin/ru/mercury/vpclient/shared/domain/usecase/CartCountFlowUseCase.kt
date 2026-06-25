package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.persistence.database.dao.CartProductDao
import javax.inject.Inject

class CartCountFlowUseCase @Inject constructor(
    private val cartProductDao: CartProductDao,
    dispatchers: SharedDispatchers
): FlowUseCase<Unit, Int>(dispatchers.io) {

    override fun execute(parameters: Unit): Flow<Int> {
        return cartProductDao.countFlow()
    }
}
