package ru.mercury.vpclient.features.fitting_address_selection.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.features.fitting_confirmation.navigation.FittingConfirmationRoute

@Serializable
data class FittingAddressSelectionRoute(
    val confirmationRoute: FittingConfirmationRoute,
    val selectedClientAddressId: Int? = null,
    val clientAddress: String? = null
): NavKey
