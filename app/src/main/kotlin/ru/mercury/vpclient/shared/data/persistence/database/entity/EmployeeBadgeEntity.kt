package ru.mercury.vpclient.shared.data.persistence.database.entity

import androidx.room.Entity

@Entity(
    tableName = "EmployeeBadge",
    primaryKeys = ["employeeId"]
)
data class EmployeeBadgeEntity(
    val employeeId: String,
    val basketIconBadge: Int,
    val basketIconNumber: Int,
    val fittingIconBadge: Int,
    val fittingIconNumber: Int,
    val messengerIconBadge: Int,
    val orderIconBadge: Int,
    val compilationIconBadge: Int
) {
    companion object {
        val Empty = EmployeeBadgeEntity(
            employeeId = "",
            basketIconBadge = 0,
            basketIconNumber = 0,
            fittingIconBadge = 0,
            fittingIconNumber = 0,
            messengerIconBadge = 0,
            orderIconBadge = 0,
            compilationIconBadge = 0
        )
    }
}
