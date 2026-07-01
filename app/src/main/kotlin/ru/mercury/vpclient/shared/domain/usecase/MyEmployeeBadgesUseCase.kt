package ru.mercury.vpclient.shared.domain.usecase

import androidx.room.withTransaction
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.persistence.database.AppDatabase
import ru.mercury.vpclient.shared.data.persistence.database.dao.EmployeeBadgeDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.EmployeeDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeBadgeEntity
import ru.mercury.vpclient.shared.domain.mapper.entity
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import javax.inject.Inject

class MyEmployeeBadgesUseCase @Inject constructor(
    private val appDatabase: AppDatabase,
    private val networkService: NetworkService,
    private val employeeDao: EmployeeDao,
    private val employeeBadgeDao: EmployeeBadgeDao,
    dispatchers: SharedDispatchers
): UseCase<Unit, Unit>(dispatchers.io) {

    override suspend fun execute(params: Unit) {
        val employeeIds = employeeDao.selectAll().map { employee -> employee.employeeId }
        if (employeeIds.isEmpty()) {
            employeeBadgeDao.delete()
            return
        }

        val badgeEntities = mutableListOf<EmployeeBadgeEntity>()

        employeeIds.forEach { employeeId ->
            handleResponse(
                request = {
                    networkService.clientEmployeeBadges(employeeId)
                },
                onSuccess = { response ->
                    badgeEntities += response.entity(employeeId)
                },
                onFailure = { error -> throw MyEmployeeBadgesException(error.message) }
            )
        }

        appDatabase.withTransaction {
            employeeBadgeDao.deleteMissing(employeeIds)
            employeeBadgeDao.upsert(badgeEntities)
        }
    }

    data class MyEmployeeBadgesException(
        override val message: String
    ): ClientException(message)
}
