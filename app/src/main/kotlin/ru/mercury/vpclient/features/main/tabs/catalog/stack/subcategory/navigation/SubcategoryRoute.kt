package ru.mercury.vpclient.features.main.tabs.catalog.stack.subcategory.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class SubcategoryRoute(
    val categoryId: Int
): NavKey
