package ru.mercury.vpclient.features.main.model

import androidx.navigation3.runtime.NavKey
import ru.mercury.vpclient.features.home_root.navigation.HomeRootRoute
import ru.mercury.vpclient.shared.mvi.Model

data class MainModel(
    val selectedRoute: NavKey = HomeRootRoute,
    val consultantsBadge: Int = 0
): Model {

    val consultantsBadgeText: String?
        get() = when {
            consultantsBadge > 0 -> consultantsBadge.toString()
            else -> null
        }
}
