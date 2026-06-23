package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.persistence.database.dao.EmployeeDao
import ru.mercury.vpclient.shared.data.persistence.database.pojo.EmployeePojo
import ru.mercury.vpclient.shared.domain.mapper.orEmpty
import javax.inject.Inject

class EmployeePojoFlowUseCase @Inject constructor(
    private val employeeDao: EmployeeDao,
    dispatchers: SharedDispatchers
): FlowUseCase<String, EmployeePojo>(dispatchers.io) {

    override fun execute(parameters: String): Flow<EmployeePojo> {
        return employeeDao.selectPojoFlow(parameters).map { employee -> employee.orEmpty }
    }
}
