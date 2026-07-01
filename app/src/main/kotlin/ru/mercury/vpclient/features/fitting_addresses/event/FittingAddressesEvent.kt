package ru.mercury.vpclient.features.fitting_addresses.event

import ru.mercury.vpclient.features.fitting_confirmation.navigation.FittingConfirmationRoute
import ru.mercury.vpclient.shared.data.persistence.database.entity.ClientDeliveryAddressEntity
import ru.mercury.vpclient.shared.mvi.Event

sealed interface FittingAddressesEvent: Event {
    data class SelectAddress(val confirmationRoute: FittingConfirmationRoute, val selectedClientAddressId: Int?, val clientAddresses: List<ClientDeliveryAddressEntity>): FittingAddressesEvent
}
