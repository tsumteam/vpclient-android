package ru.mercury.vpclient.features.consultant.model

import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.mvi.Model

data class ConsultantModel(
    val employeeEntity: EmployeeEntity = EmployeeEntity.Empty
): Model {
    val photoUrl: String
        get() = employeeEntity.photoUrl.ifEmpty { employeeEntity.previewPhotoUrl }

    val boutiqueAddress: String
        get() = employeeEntity.employeeBotiqueAddressShort.ifEmpty { employeeEntity.employeeBotiqueAddress }
}
