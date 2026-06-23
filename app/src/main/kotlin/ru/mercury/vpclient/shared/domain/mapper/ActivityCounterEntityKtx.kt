package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.persistence.database.entity.ActivityCounterEntity

fun ActivityCounterEntity?.orEmpty(type: String): ActivityCounterEntity {
    return this ?: ActivityCounterEntity(
        type = type,
        value = 0
    )
}
