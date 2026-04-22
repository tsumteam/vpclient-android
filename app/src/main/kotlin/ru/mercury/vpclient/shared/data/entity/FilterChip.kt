package ru.mercury.vpclient.shared.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class FilterChip(
    val id: String,
    val label: String
)
