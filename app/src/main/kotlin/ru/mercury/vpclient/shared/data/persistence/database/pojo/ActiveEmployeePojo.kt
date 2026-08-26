package ru.mercury.vpclient.shared.data.persistence.database.pojo

import androidx.room.Embedded
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity

data class ActiveEmployeePojo(
    @Embedded val entity: EmployeeEntity,
    val messengerCounter: Int
)
