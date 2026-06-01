package ru.mercury.vpclient.features.filter.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.entity.BrandEntity
import ru.mercury.vpclient.shared.data.entity.FilterChip

@Serializable
data class FilterRoute(
    val categoryId: Int,
    val titleCategoryId: Int,
    val subtitleCategoryId: Int,
    val isSingleLineTitle: Boolean = false,
    val brandEntity: BrandEntity? = null,
    val initialSelectedFilterValueChips: List<FilterChip> = emptyList(),
    val hiddenFilterValueChipIds: List<String> = emptyList(),
    val viewTypeOverride: String? = null
): NavKey
