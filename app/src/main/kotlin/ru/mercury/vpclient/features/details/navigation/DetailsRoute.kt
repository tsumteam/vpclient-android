package ru.mercury.vpclient.features.details.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

// не data class, чтобы открывать одинаковые экраны в nav3
@Serializable
class DetailsRoute(
    val id: String,
    val itemId: String? = null,
    val colorId: String? = null,
    val openedFromCart: Boolean = false,
    val isBrandRoot: Boolean = false,
    val isHomeRoot: Boolean = false,
    val isMainRoot: Boolean = false
): NavKey
