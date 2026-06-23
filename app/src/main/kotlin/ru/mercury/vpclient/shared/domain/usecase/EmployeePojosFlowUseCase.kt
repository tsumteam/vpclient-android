package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.persistence.database.dao.EmployeeDao
import ru.mercury.vpclient.shared.data.persistence.database.pojo.EmployeePojo
import javax.inject.Inject

class EmployeePojosFlowUseCase @Inject constructor(
    private val employeeDao: EmployeeDao,
    dispatchers: SharedDispatchers
): FlowUseCase<Unit, List<EmployeePojo>>(dispatchers.io) {

    override fun execute(parameters: Unit): Flow<List<EmployeePojo>> {
        return employeeDao.selectAllPojosFlow()
    }
}
