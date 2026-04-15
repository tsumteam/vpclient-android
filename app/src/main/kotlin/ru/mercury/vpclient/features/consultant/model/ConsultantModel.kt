package ru.mercury.vpclient.features.consultant.model

import ru.mercury.vpclient.shared.mvi.Model
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity

data class ConsultantModel(
    val employeeEntity: EmployeeEntity = EmployeeEntity.Empty
): Model
