package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.persistence.database.pojo.EmployeePojo

val EmployeePojo?.orEmpty: EmployeePojo
    get() = this ?: EmployeePojo.Empty

val EmployeePojo.employeeFullName: String
    get() = entity.employeeFullName

val EmployeePojo.isPhotoEmpty: Boolean
    get() = entity.isPhotoEmpty

val EmployeePojo.boutiqueAddress: String
    get() = entity.boutiqueAddress

val EmployeePojo.hasBasketBadge: Boolean
    get() = basketBadge > 0

val EmployeePojo.basketText: String
    get() = if (basketNumber > 0) basketNumber.toString() else ""

val EmployeePojo.hasFittingProducts: Boolean
    get() = fittingNumber > 0

val EmployeePojo.fittingText: String
    get() = if (hasFittingProducts) fittingNumber.toString() else ""

val EmployeePojo.hasFittingBadge: Boolean
    get() = fittingBadge > 0

val EmployeePojo.hasMessengerBadge: Boolean
    get() = messengerBadge > 0

val EmployeePojo.basketNumber: Int
    get() = badgeEntity?.basketIconNumber ?: 0

val EmployeePojo.basketBadge: Int
    get() = badgeEntity?.basketIconBadge ?: 0

val EmployeePojo.fittingNumber: Int
    get() = badgeEntity?.fittingIconNumber ?: 0

val EmployeePojo.fittingBadge: Int
    get() = badgeEntity?.fittingIconBadge ?: 0

val EmployeePojo.messengerBadge: Int
    get() = badgeEntity?.messengerIconBadge ?: 0

val EmployeePojo.orderBadge: Int
    get() = badgeEntity?.orderIconBadge ?: 0

val EmployeePojo.compilationBadge: Int
    get() = badgeEntity?.compilationIconBadge ?: 0
