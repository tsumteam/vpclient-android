package ru.mercury.vpclient.features.compilation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class CompilationRoute(
    val id: Int
): NavKey
