package ru.mercury.vpclient.features.details.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

// не data-class чтобы открывать одинаковые экраны в nav3
@Serializable
class DetailsRoute(
    val id: String
): NavKey
