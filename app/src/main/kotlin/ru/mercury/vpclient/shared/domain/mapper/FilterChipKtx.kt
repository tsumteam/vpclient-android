package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.entity.FilterChip

infix fun List<FilterChip>.has(id: String): Boolean {
    return any { selectedChip -> selectedChip.id == id }
}
