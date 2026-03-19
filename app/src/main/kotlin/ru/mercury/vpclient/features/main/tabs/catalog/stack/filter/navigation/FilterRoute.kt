package ru.mercury.vpclient.features.main.tabs.catalog.stack.filter.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class FilterRoute(
    val categoryId: Int,
    val titleCategoryId: Int,
    val subtitleCategoryId: Int
): NavKey
