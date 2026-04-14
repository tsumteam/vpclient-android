package ru.mercury.vpclient.shared.persistence.database.entity

import androidx.room.Entity

@Entity(
    tableName = "Employee",
    primaryKeys = ["employeeId"]
)
data class EmployeeEntity(
    val employeeId: String,
    val employeeEmail: String,
    val employeeMiddleName: String,
    val employeeName: String,
    val employeePhone: String,
    val employeeSurname: String,
    val photoUrl: String,
    val previewPhotoUrl: String,
    val lastActivityColorHex: String,
    val lastActivityDate: String,
    val employeeBotiqueAddress: String,
    val employeeBotiqueAddressShort: String,
    val employeeBrand: String,
    val isActive: Boolean,
    val basketBadge: Int,
    val fittingBadge: Int,
    val messengerBadge: Int,
    val orderBadge: Int,
    val compilationBadge: Int
) {
    companion object {
        const val ID_CALL = 0
        const val ID_FITTING = 0
        const val ID_CART = 0
        const val ID_CHAT = 0
        const val ID_SELECTION = 0

        val Empty = EmployeeEntity(
            employeeId = "",
            employeeEmail = "",
            employeeMiddleName = "",
            employeeName = "",
            employeePhone = "",
            employeeSurname = "",
            photoUrl = "",
            previewPhotoUrl = "",
            lastActivityColorHex = "",
            lastActivityDate = "",
            employeeBotiqueAddress = "",
            employeeBotiqueAddressShort = "",
            employeeBrand = "",
            isActive = false,
            basketBadge = 0,
            fittingBadge = 0,
            messengerBadge = 0,
            orderBadge = 0,
            compilationBadge = 0
        )
    }
}
