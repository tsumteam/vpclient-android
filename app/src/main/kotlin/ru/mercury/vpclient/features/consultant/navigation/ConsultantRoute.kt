package ru.mercury.vpclient.features.consultant.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class ConsultantRoute(
    val consultantId: String
): NavKey
