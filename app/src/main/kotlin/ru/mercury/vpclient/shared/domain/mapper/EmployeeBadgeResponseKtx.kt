package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.network.response.EmployeeBadgeResponse
import ru.mercury.vpclient.shared.data.network.response.EmployeeBadgesResponse
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeBadgeEntity

fun EmployeeBadgesResponse.entity(employeeId: String): EmployeeBadgeEntity {
    return EmployeeBadgeEntity(
        employeeId = this.employeeId.orEmpty().ifEmpty { employeeId },
        basketIconBadge = basketIcon.badge(),
        basketIconNumber = basketIcon.number(),
        fittingIconBadge = fittingIcon.badge(),
        fittingIconNumber = fittingIcon.number(),
        messengerIconBadge = messengerIcon.badge(),
        orderIconBadge = orderIcon.badge(),
        compilationIconBadge = compilationIcon.badge()
    )
}

private fun EmployeeBadgeResponse?.badge(): Int {
    return this?.badge.orEmpty
}

private fun EmployeeBadgeResponse?.number(): Int {
    return this?.icon?.number.orEmpty
}
