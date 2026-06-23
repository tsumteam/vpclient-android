package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity

val EmployeeEntity?.orEmpty: EmployeeEntity
    get() = this ?: EmployeeEntity.Empty

val EmployeeEntity.isNotEmpty: Boolean
    get() = this != EmployeeEntity.Empty

val EmployeeEntity.employeeFullName: String
    get() = "$employeeName $employeeSurname".trim()

val EmployeeEntity.isPhotoEmpty: Boolean
    get() = previewPhotoUrl.isEmpty()

val EmployeeEntity.boutiqueAddress: String
    get() = employeeBotiqueAddressShort.ifEmpty { employeeBotiqueAddress }

val EmployeeEntity.hasBasketBadge: Boolean
    get() = basketBadge > 0

val EmployeeEntity.basketText: String
    get() = if (basketNumber > 0) basketNumber.toString() else ""

val EmployeeEntity.hasFittingProducts: Boolean
    get() = fittingNumber > 0

val EmployeeEntity.fittingText: String
    get() = if (hasFittingProducts) fittingNumber.toString() else ""

val EmployeeEntity.hasFittingBadge: Boolean
    get() = fittingBadge > 0

val EmployeeEntity.hasMessengerBadge: Boolean
    get() = messengerBadge > 0
