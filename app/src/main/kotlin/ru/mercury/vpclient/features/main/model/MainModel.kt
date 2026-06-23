package ru.mercury.vpclient.features.main.model

import androidx.navigation3.runtime.NavKey
import ru.mercury.vpclient.features.home.navigation.HomeRoute
import ru.mercury.vpclient.shared.mvi.Model

data class MainModel(
    val selectedRoute: NavKey = HomeRoute,
    val consultantsBadge: Int = 0
): Model {

    val consultantsBadgeText: String?
        get() = when {
            consultantsBadge > 0 -> consultantsBadge.toString()
            else -> null
        }
}
