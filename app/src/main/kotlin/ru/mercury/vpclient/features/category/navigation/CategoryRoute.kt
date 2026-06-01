package ru.mercury.vpclient.features.category.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class CategoryRoute(
    val categoryId: Int
): NavKey
