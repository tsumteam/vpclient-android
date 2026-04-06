package ru.mercury.vpclient.features.details.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class DetailsRoute(
    val id: String
): NavKey
