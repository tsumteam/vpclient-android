package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.network.response.EmployeeBadgeResponse
import ru.mercury.vpclient.shared.data.network.response.EmployeeBadgesResponse
import ru.mercury.vpclient.shared.data.network.response.EmployeeResponse
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity

fun EmployeeResponse.entity(
    current: EmployeeEntity,
    badges: EmployeeBadgesResponse,
    isActive: Boolean,
    position: Int = current.position
): EmployeeEntity {
    fun EmployeeBadgeResponse?.badge(): Int {
        return this?.badge.orEmpty
    }

    fun EmployeeBadgeResponse?.number(): Int {
        return this?.icon?.number.orEmpty
    }

    return EmployeeEntity(
        employeeId = employeeId.orEmpty().ifEmpty { current.employeeId },
        employeeEmail = employeeEmail.orEmpty().ifEmpty { current.employeeEmail },
        employeeMiddleName = employeeMiddleName.orEmpty().ifEmpty { current.employeeMiddleName },
        employeeName = employeeName.orEmpty().ifEmpty { current.employeeName },
        employeePhone = employeePhone.orEmpty().ifEmpty { current.employeePhone },
        employeeSurname = employeeSurname.orEmpty().ifEmpty { current.employeeSurname },
        photoUrl = photoUrl.orEmpty().ifEmpty { current.photoUrl },
        previewPhotoUrl = previewPhotoUrl.orEmpty().ifEmpty { current.previewPhotoUrl },
        lastActivityColorHex = lastActivity?.colorHex.orEmpty().ifEmpty { current.lastActivityColorHex },
        lastActivityDate = lastActivity?.date.orEmpty().ifEmpty { current.lastActivityDate },
        employeeBotiqueAddress = employeeBotiqueAddress.orEmpty().ifEmpty { current.employeeBotiqueAddress },
        employeeBotiqueAddressShort = employeeBotiqueAddressShort.orEmpty().ifEmpty { current.employeeBotiqueAddressShort },
        employeeBrand = employeeBrand.orEmpty().ifEmpty { current.employeeBrand },
        isActive = isActive,
        position = position,
        basketNumber = badges.basketIcon.number(),
        basketBadge = badges.basketIcon.badge(),
        fittingNumber = badges.fittingIcon.number(),
        fittingBadge = badges.fittingIcon.badge(),
        messengerBadge = badges.messengerIcon.badge(),
        orderBadge = badges.orderIcon.badge(),
        compilationBadge = badges.compilationIcon.badge()
    )
}

fun EmployeeResponse.entity(
    current: EmployeeEntity,
    isActive: Boolean,
    position: Int = current.position
): EmployeeEntity {
    return EmployeeEntity(
        employeeId = employeeId.orEmpty().ifEmpty { current.employeeId },
        employeeEmail = employeeEmail.orEmpty().ifEmpty { current.employeeEmail },
        employeeMiddleName = employeeMiddleName.orEmpty().ifEmpty { current.employeeMiddleName },
        employeeName = employeeName.orEmpty().ifEmpty { current.employeeName },
        employeePhone = employeePhone.orEmpty().ifEmpty { current.employeePhone },
        employeeSurname = employeeSurname.orEmpty().ifEmpty { current.employeeSurname },
        photoUrl = photoUrl.orEmpty().ifEmpty { current.photoUrl },
        previewPhotoUrl = previewPhotoUrl.orEmpty().ifEmpty { current.previewPhotoUrl },
        lastActivityColorHex = lastActivity?.colorHex.orEmpty().ifEmpty { current.lastActivityColorHex },
        lastActivityDate = lastActivity?.date.orEmpty().ifEmpty { current.lastActivityDate },
        employeeBotiqueAddress = employeeBotiqueAddress.orEmpty().ifEmpty { current.employeeBotiqueAddress },
        employeeBotiqueAddressShort = employeeBotiqueAddressShort.orEmpty().ifEmpty { current.employeeBotiqueAddressShort },
        employeeBrand = employeeBrand.orEmpty().ifEmpty { current.employeeBrand },
        isActive = isActive,
        position = position,
        basketNumber = current.basketNumber,
        basketBadge = current.basketBadge,
        fittingNumber = current.fittingNumber,
        fittingBadge = current.fittingBadge,
        messengerBadge = current.messengerBadge,
        orderBadge = current.orderBadge,
        compilationBadge = current.compilationBadge
    )
}
