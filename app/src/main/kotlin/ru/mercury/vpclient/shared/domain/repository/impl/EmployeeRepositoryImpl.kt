package ru.mercury.vpclient.shared.domain.repository.impl

import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.domain.mapper.entity
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.persistence.database.dao.EmployeeDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.repository.EmployeeRepository
import javax.inject.Inject

class EmployeeRepositoryImpl @Inject constructor(
    private val networkService: NetworkService,
    private val employeeDao: EmployeeDao,
    private val settingsDataStore: SettingsDataStore
): EmployeeRepository {

    override val employeeEntitiesFlow: Flow<List<EmployeeEntity>> = employeeDao.selectAllFlow()

    override fun employeeEntityFlow(employeeId: String): Flow<EmployeeEntity> {
        return employeeDao.selectFlow(employeeId)
    }

    override suspend fun syncEmployees() {
        val employees = handleResponseResult {
            networkService.clientMyEmployees()
        }.getOrThrow().items

        if (employees.isEmpty()) {
            employeeDao.clear()
            return
        }

        val activeEmployeeId = handleResponseResult {
            networkService.clientActiveEmployee()
        }.getOrNull()?.employeeId.orEmpty()

        settingsDataStore.setValue(PreferenceKey.PairedUser, activeEmployeeId)

        val entities = employees.map { employee ->
            val badges = handleResponseResult {
                networkService.clientEmployeeBadges(employee.employeeId.orEmpty())
            }.getOrThrow()

            employee.entity(
                current = employeeDao.select(employee.employeeId.orEmpty()) ?: EmployeeEntity.Empty,
                badges = badges,
                isActive = employee.employeeId == activeEmployeeId
            )
        }

        employeeDao.removeMissing(entities.map(EmployeeEntity::employeeId))
        employeeDao.upsert(entities)
    }

    override suspend fun syncEmployee(employeeId: String) {
        val current = employeeDao.select(employeeId) ?: EmployeeEntity.Empty.copy(employeeId = employeeId)

        val employee = handleResponseResult {
            networkService.clientEmployee(employeeId)
        }.getOrThrow()

        val badges = handleResponseResult {
            networkService.clientEmployeeBadges(employeeId)
        }.getOrThrow()

        val activeEmployeeId = handleResponseResult {
            networkService.clientActiveEmployee()
        }.getOrNull()?.employeeId.orEmpty()

        employeeDao.upsert(
            employee.entity(
                current = current,
                badges = badges,
                isActive = employeeId == activeEmployeeId
            )
        )
    }
}
