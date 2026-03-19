package ru.mercury.vpclient.core.ktx

import ru.mercury.vpclient.core.entity.FilterChip

infix fun List<FilterChip>.has(id: String): Boolean {
    return any { selectedChip -> selectedChip.id == id }
}
