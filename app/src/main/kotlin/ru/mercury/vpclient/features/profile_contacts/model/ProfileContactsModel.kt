package ru.mercury.vpclient.features.profile_contacts.model

import ru.mercury.vpclient.shared.data.CLIENT_SERVICE_EMAIL
import ru.mercury.vpclient.shared.data.CLIENT_SERVICE_PHONE
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.mvi.Model

data class ProfileContactsModel(
    val activeEmployee: EmployeeEntity = EmployeeEntity.Empty
): Model {

    val consultantPhone: String
        get() = activeEmployee.employeePhone

    val customerServicePhone: String
        get() = CLIENT_SERVICE_PHONE

    val customerServiceEmail: String
        get() = CLIENT_SERVICE_EMAIL
}
