package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.entity.DetailsField

fun String.toField(factory: (String) -> DetailsField): DetailsField? {
    return trim().takeIf { it.isNotEmpty() }?.let(factory)
}
