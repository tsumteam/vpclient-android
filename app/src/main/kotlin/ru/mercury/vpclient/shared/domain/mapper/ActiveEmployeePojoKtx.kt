package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.data.persistence.database.pojo.ActiveEmployeePojo

val ActiveEmployeePojo?.entityWithMessengerBadge: EmployeeEntity
    get() = this?.entity?.copy(messengerBadge = messengerCounter) ?: EmployeeEntity.Empty
