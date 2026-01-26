package ru.mercury.vpclient.features.main.navigation

import kotlinx.serialization.Serializable
import ru.mercury.vpclient.core.navigation.Route

@Serializable
data class MainRoute(
    val popUpToMain: Boolean = false
): Route
