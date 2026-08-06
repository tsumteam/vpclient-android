package ru.mercury.vpclient.features.fitting_addresses.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class FittingAddressesRoute(
    val origin: FittingAddressesOrigin,
    val selectedClientAddressId: Int? = null,
    val clientAddress: String? = null
): NavKey
