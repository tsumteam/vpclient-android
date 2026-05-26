package ru.mercury.vpclient.features.main.navigation

import kotlinx.serialization.Serializable
import androidx.navigation3.runtime.NavKey

@Serializable
data class MainRoute(
    val popUpToMain: Boolean = false,
    val selectedTab: String? = null
): NavKey {
    companion object {
        const val CATALOG_TAB = "catalog"
        const val FITTING_TAB = "fitting"
    }
}
