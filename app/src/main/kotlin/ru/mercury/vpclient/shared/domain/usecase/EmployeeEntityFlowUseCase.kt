@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.persistence.database.dao.EmployeeDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.domain.mapper.orEmpty
import javax.inject.Inject

class EmployeeEntityFlowUseCase @Inject constructor(
    private val employeeDao: EmployeeDao,
    dispatchers: SharedDispatchers
): FlowUseCase<String, EmployeeEntity>(dispatchers.io) {

    override fun execute(employeeId: String): Flow<EmployeeEntity> {
        return employeeDao.selectFlow(employeeId).map { employee -> employee.orEmpty }
    }
}
