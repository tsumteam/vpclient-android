package ru.mercury.vpclient.features.main.tabs.home.model

import ru.mercury.vpclient.shared.mvi.Model

data class HomeModel(
    val cartSize: Int = 0,
    val cartBadge: Int = 0
): Model {

    val cartText: String
        get() = when {
            cartSize > 0 -> cartSize.toString()
            else -> ""
        }

    val showCartBadge: Boolean
        get() = cartBadge > 0
}
