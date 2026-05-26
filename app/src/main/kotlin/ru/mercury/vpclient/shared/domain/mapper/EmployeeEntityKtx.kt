package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity

val EmployeeEntity.hasBasketBadge: Boolean
    get() = basketBadge > 0

val EmployeeEntity.basketText: String
    get() = if (hasBasketBadge) basketBadge.toString() else ""

val EmployeeEntity.hasFittingProducts: Boolean
    get() = fittingNumber > 0

val EmployeeEntity.fittingText: String
    get() = if (hasFittingProducts) fittingNumber.toString() else ""

val EmployeeEntity.hasFittingBadge: Boolean
    get() = fittingBadge > 0

val EmployeeEntity.hasMessengerBadge: Boolean
    get() = messengerBadge > 0
