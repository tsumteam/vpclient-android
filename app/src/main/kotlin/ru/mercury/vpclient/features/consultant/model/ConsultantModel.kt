package ru.mercury.vpclient.features.consultant.model

import ru.mercury.vpclient.shared.data.persistence.database.pojo.EmployeePojo
import ru.mercury.vpclient.shared.mvi.Model

data class ConsultantModel(
    val employeePojo: EmployeePojo = EmployeePojo.Empty
): Model
