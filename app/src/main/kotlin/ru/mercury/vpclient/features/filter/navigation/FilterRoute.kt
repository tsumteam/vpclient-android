package ru.mercury.vpclient.features.filter.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.entity.BrandEntity
import ru.mercury.vpclient.shared.data.entity.FilterChip
import ru.mercury.vpclient.shared.data.network.type.CatalogViewType

@Serializable
data class FilterRoute(
    val categoryId: Int,
    val titleCategoryId: Int,
    val subtitleCategoryId: Int,
    val titleOverride: String? = null,
    val isSingleLineTitle: Boolean = false,
    val isFavoriteBrands: Boolean = false,
    val brandEntity: BrandEntity? = null,
    val initialSelectedFilterValueChips: List<FilterChip> = emptyList(),
    val hiddenFilterValueChipIds: List<String> = emptyList(),
    val viewTypeOverride: CatalogViewType? = null,
    val actionId: Int? = null,
    val isBrandRoot: Boolean = false,
    val isHomeRoot: Boolean = false,
    val isMainRoot: Boolean = false
): NavKey
