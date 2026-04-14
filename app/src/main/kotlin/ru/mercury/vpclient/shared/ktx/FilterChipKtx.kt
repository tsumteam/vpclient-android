package ru.mercury.vpclient.shared.ktx

import ru.mercury.vpclient.shared.entity.FilterChip

infix fun List<FilterChip>.has(id: String): Boolean {
    return any { selectedChip -> selectedChip.id == id }
}
