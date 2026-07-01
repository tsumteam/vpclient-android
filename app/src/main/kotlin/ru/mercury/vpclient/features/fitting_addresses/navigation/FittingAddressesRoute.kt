package ru.mercury.vpclient.features.fitting_addresses.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.features.fitting_confirmation.navigation.FittingConfirmationRoute

@Serializable
data class FittingAddressesRoute(
    val confirmationRoute: FittingConfirmationRoute,
    val selectedClientAddressId: Int? = null,
    val clientAddress: String? = null
): NavKey
