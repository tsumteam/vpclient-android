package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.persistence.database.dao.FittingProductDao
import javax.inject.Inject

class FittingCountFlowUseCase @Inject constructor(
    private val fittingProductDao: FittingProductDao,
    dispatchers: SharedDispatchers
): FlowUseCase<Unit, Int>(dispatchers.io) {

    override fun execute(parameters: Unit): Flow<Int> {
        return fittingProductDao.countFlow()
    }
}
