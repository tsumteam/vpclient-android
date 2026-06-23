package ru.mercury.vpclient.features.consultants.model

import kotlinx.coroutines.Job
import ru.mercury.vpclient.shared.data.persistence.database.pojo.EmployeePojo
import ru.mercury.vpclient.shared.mvi.Model

data class ConsultantsModel(
    val employeePojos: List<EmployeePojo> = emptyList(),
    val loadJob: Job? = null
): Model {

    val isLoading: Boolean
        get() = loadJob?.isActive == true
}
