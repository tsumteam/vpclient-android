package ru.mercury.vpclient.core.interactor.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.mercury.vpclient.core.coroutines.ClientDispatchers
import ru.mercury.vpclient.core.interactor.EmployeeInteractor
import ru.mercury.vpclient.core.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.core.repository.EmployeeRepository
import javax.inject.Inject

class EmployeeInteractorImpl @Inject constructor(
    private val dispatchers: ClientDispatchers,
    private val employeeRepository: EmployeeRepository
): EmployeeInteractor {

    override val employeeEntitiesFlow: Flow<List<EmployeeEntity>> = employeeRepository.employeeEntitiesFlow

    override fun employeeEntityFlow(employeeId: String): Flow<EmployeeEntity> {
        return employeeRepository.employeeEntityFlow(employeeId)
    }

    override suspend fun syncEmployees() {
        withContext(dispatchers.io) { employeeRepository.syncEmployees() }
    }

    override suspend fun syncEmployee(employeeId: String) {
        withContext(dispatchers.io) { employeeRepository.syncEmployee(employeeId) }
    }
}
