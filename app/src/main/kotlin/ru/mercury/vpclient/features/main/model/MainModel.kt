package ru.mercury.vpclient.features.main.model

import ru.mercury.vpclient.shared.mvi.Model
import androidx.navigation3.runtime.NavKey
import ru.mercury.vpclient.features.home.navigation.HomeRoute

data class MainModel(
    val selectedRoute: NavKey = HomeRoute
): Model
