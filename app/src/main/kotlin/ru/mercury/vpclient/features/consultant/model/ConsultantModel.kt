package ru.mercury.vpclient.features.consultant.model

import ru.mercury.vpclient.core.mvi.Model
import ru.mercury.vpclient.core.persistence.database.entity.EmployeeEntity

data class ConsultantModel(
    val employeeEntity: EmployeeEntity = EmployeeEntity.Empty
): Model
