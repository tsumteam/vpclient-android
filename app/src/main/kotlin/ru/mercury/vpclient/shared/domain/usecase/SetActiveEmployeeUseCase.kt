@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.persistence.database.dao.EmployeeDao
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import javax.inject.Inject

class SetActiveEmployeeUseCase @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
    private val employeeDao: EmployeeDao,
    dispatchers: SharedDispatchers
): UseCase<String, Unit>(dispatchers.io) {

    override suspend fun execute(employeeId: String) {
        val employee = employeeDao.selectNotNull(employeeId)
        settingsDataStore.setValue(PreferenceKey.PairedUser, employee.employeeId)
        employeeDao.setActive(employee.employeeId)
    }
}
