package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity

val EmployeeEntity.hasBasketBadge: Boolean
    get() = basketBadge > 0

val EmployeeEntity.basketText: String
    get() = if (hasBasketBadge) basketBadge.toString() else ""
