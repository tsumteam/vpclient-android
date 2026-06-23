package ru.mercury.vpclient.shared.domain.usecase

import androidx.room.withTransaction
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.error.MyEmployeesException
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.persistence.database.AppDatabase
import ru.mercury.vpclient.shared.data.persistence.database.dao.EmployeeDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.entity
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import javax.inject.Inject

class MyEmployeesUseCase @Inject constructor(
    private val appDatabase: AppDatabase,
    private val networkService: NetworkService,
    private val employeeDao: EmployeeDao,
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): UseCase<Unit, Unit>(dispatchers.io) {

    override suspend fun execute(params: Unit) {
        handleResponse(
            request = {
                networkService.clientMyEmployees()
            },
            onSuccess = { response ->
                val activeEmployeeId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
                val employeeEntities = response.items.mapIndexedNotNull { position, employee ->
                    val employeeId = employee.employeeId.orEmpty()
                    when {
                        employeeId.isEmpty() -> null
                        else -> {
                            val current = employeeDao.select(employeeId)
                                ?: EmployeeEntity.Empty.copy(employeeId = employeeId)
                            employee.entity(
                                current = current,
                                isActive = employeeId == activeEmployeeId,
                                position = position
                            )
                        }
                    }
                }
                when {
                    employeeEntities.isEmpty() -> employeeDao.delete()
                    else -> {
                        appDatabase.withTransaction {
                            employeeDao.deleteMissing(employeeEntities.map(EmployeeEntity::employeeId))
                            employeeDao.upsert(employeeEntities)
                        }
                    }
                }
            },
            onFailure = { error -> throw MyEmployeesException(error.message) }
        )
    }
}
