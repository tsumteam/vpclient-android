package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.persistence.database.dao.EmployeeDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.domain.mapper.orEmpty
import javax.inject.Inject

class EmployeeActiveFlowUseCase @Inject constructor(
    private val employeeDao: EmployeeDao,
    dispatchers: SharedDispatchers
): FlowUseCase<Unit, EmployeeEntity>(dispatchers.io) {

    override fun execute(parameters: Unit): Flow<EmployeeEntity> {
        return employeeDao.selectActiveFlow().map { employee -> employee.orEmpty }
    }
}
