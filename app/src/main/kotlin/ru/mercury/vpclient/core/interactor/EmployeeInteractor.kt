package ru.mercury.vpclient.core.interactor

import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.core.persistence.database.entity.EmployeeEntity

interface EmployeeInteractor {

    val employeeEntitiesFlow: Flow<List<EmployeeEntity>>

    fun employeeEntityFlow(employeeId: String): Flow<EmployeeEntity>

    suspend fun syncEmployees()

    suspend fun syncEmployee(employeeId: String)
}
