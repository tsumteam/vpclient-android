package ru.mercury.vpclient.core.repository.impl

import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.core.ktx.handleResponseResult
import ru.mercury.vpclient.core.network.NetworkService
import ru.mercury.vpclient.core.network.response.EmployeeBadgeResponse
import ru.mercury.vpclient.core.network.response.EmployeeBadgesResponse
import ru.mercury.vpclient.core.network.response.EmployeeResponse
import ru.mercury.vpclient.core.persistence.database.dao.EmployeeDao
import ru.mercury.vpclient.core.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.core.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.core.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.core.repository.EmployeeRepository
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

private fun EmployeeResponse.entity(
    current: EmployeeEntity,
    badges: EmployeeBadgesResponse,
    isActive: Boolean
): EmployeeEntity {
    fun EmployeeBadgeResponse?.badge(): Int = this?.badge ?: 0

    return EmployeeEntity(
        employeeId = employeeId.orEmpty().ifEmpty { current.employeeId },
        employeeEmail = employeeEmail.orEmpty().ifEmpty { current.employeeEmail },
        employeeMiddleName = employeeMiddleName.orEmpty().ifEmpty { current.employeeMiddleName },
        employeeName = employeeName.orEmpty().ifEmpty { current.employeeName },
        employeePhone = employeePhone.orEmpty().ifEmpty { current.employeePhone },
        employeeSurname = employeeSurname.orEmpty().ifEmpty { current.employeeSurname },
        photoUrl = photoUrl.orEmpty().ifEmpty { current.photoUrl },
        previewPhotoUrl = previewPhotoUrl.orEmpty().ifEmpty { current.previewPhotoUrl },
        lastActivityColorHex = lastActivity?.colorHex.orEmpty().ifEmpty { current.lastActivityColorHex },
        lastActivityDate = lastActivity?.date.orEmpty().ifEmpty { current.lastActivityDate },
        employeeBotiqueAddress = employeeBotiqueAddress.orEmpty().ifEmpty { current.employeeBotiqueAddress },
        employeeBotiqueAddressShort = employeeBotiqueAddressShort.orEmpty().ifEmpty { current.employeeBotiqueAddressShort },
        employeeBrand = employeeBrand.orEmpty().ifEmpty { current.employeeBrand },
        isActive = isActive,
        basketBadge = badges.basketIcon.badge(),
        fittingBadge = badges.fittingIcon.badge(),
        messengerBadge = badges.messengerIcon.badge(),
        orderBadge = badges.orderIcon.badge(),
        compilationBadge = badges.compilationIcon.badge()
    )
}
