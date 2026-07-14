package ru.mercury.vpclient.features.banner.model

import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.domain.mapper.hasFittingBadge
import ru.mercury.vpclient.shared.domain.mapper.hasMessengerBadge
import ru.mercury.vpclient.shared.mvi.Model

data class BannerModel(
    val url: String = "",
    val cartBadge: Int = 0,
    val activeEmployee: EmployeeEntity = EmployeeEntity.Empty
): Model {

    val isCartBadgeVisible: Boolean
        get() = cartBadge > 0

    val isFittingBadgeVisible: Boolean
        get() = activeEmployee.hasFittingBadge

    val isMessengerBadgeVisible: Boolean
        get() = activeEmployee.hasMessengerBadge
}
