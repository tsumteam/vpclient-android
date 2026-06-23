@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.error.MyEmployeeException
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.persistence.database.dao.EmployeeDao
import ru.mercury.vpclient.shared.domain.mapper.entity
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import javax.inject.Inject

class MyEmployeeUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val employeeDao: EmployeeDao,
    dispatchers: SharedDispatchers
): UseCase<String, Unit>(dispatchers.io) {

    override suspend fun execute(employeeId: String) {
        handleResponse(
            request = {
                networkService.clientEmployee(employeeId)
            },
            onSuccess = { response ->
                val employeeId = response.employeeId.orEmpty()
                val currentEmployeeEntity = employeeDao.selectNotNull(employeeId)
                val employeeEntity = response.entity(
                    current = currentEmployeeEntity,
                    isActive = currentEmployeeEntity.isActive
                )
                employeeDao.upsert(employeeEntity)
            },
            onFailure = { error -> throw MyEmployeeException(error.message) }
        )
    }
}
