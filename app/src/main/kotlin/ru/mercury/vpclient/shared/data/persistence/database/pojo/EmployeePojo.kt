package ru.mercury.vpclient.shared.data.persistence.database.pojo

import androidx.room.Embedded
import androidx.room.Relation
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeBadgeEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity

data class EmployeePojo(
    @Embedded val entity: EmployeeEntity,
    @Relation(
        parentColumn = "employeeId",
        entityColumn = "employeeId",
        entity = EmployeeBadgeEntity::class
    ) val badgeEntity: EmployeeBadgeEntity?
) {
    companion object {
        val Empty = EmployeePojo(
            entity = EmployeeEntity.Empty,
            badgeEntity = EmployeeBadgeEntity.Empty
        )
    }
}
