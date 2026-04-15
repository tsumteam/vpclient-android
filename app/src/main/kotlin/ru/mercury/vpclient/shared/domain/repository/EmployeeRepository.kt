package ru.mercury.vpclient.shared.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity

interface EmployeeRepository {

    val employeeEntitiesFlow: Flow<List<EmployeeEntity>>

    fun employeeEntityFlow(employeeId: String): Flow<EmployeeEntity>

    suspend fun syncEmployees()

    suspend fun syncEmployee(employeeId: String)
}
