package ru.mercury.vpclient.features.fitting_addresses.navigation

import kotlinx.serialization.Serializable
import ru.mercury.vpclient.features.fitting_confirmation.navigation.FittingConfirmationRoute

@Serializable
sealed interface FittingAddressesOrigin {

    @Serializable
    data class Fitting(val confirmationRoute: FittingConfirmationRoute): FittingAddressesOrigin

    @Serializable
    data object Checkout: FittingAddressesOrigin
}
