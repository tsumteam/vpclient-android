package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.persistence.database.entity.LoyaltyCardInfoEntity

val LoyaltyCardInfoEntity?.orEmpty: LoyaltyCardInfoEntity
    get() = this ?: LoyaltyCardInfoEntity.Empty
