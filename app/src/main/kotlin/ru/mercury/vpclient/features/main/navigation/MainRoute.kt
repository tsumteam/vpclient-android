package ru.mercury.vpclient.features.main.navigation

import kotlinx.serialization.Serializable
import androidx.navigation3.runtime.NavKey

@Serializable
data class MainRoute(
    val popUpToMain: Boolean = false
): NavKey
