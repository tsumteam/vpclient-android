package ru.mercury.vpclient.features.main.tabs.consultants.model

import kotlinx.coroutines.Job
import ru.mercury.vpclient.shared.mvi.Model
import ru.mercury.vpclient.shared.persistence.database.entity.EmployeeEntity

data class ConsultantsModel(
    val employees: List<EmployeeEntity> = emptyList(),
    val loadConsultantsJob: Job? = null
): Model {

    val isLoading: Boolean
        get() = loadConsultantsJob?.isActive == true
}
