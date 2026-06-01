package ru.mercury.vpclient.features.fitting_address_selection.event

import ru.mercury.vpclient.features.fitting_confirmation.navigation.FittingConfirmationRoute
import ru.mercury.vpclient.shared.data.entity.ClientDeliveryAddress

data class FittingAddressSelectionResult(
    val confirmationRoute: FittingConfirmationRoute,
    val selectedClientAddressId: Int?,
    val clientAddresses: List<ClientDeliveryAddress>
)
