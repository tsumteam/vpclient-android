package ru.mercury.vpclient.features.fitting_addresses.event

import ru.mercury.vpclient.features.fitting_addresses.navigation.FittingAddressesOrigin
import ru.mercury.vpclient.shared.data.persistence.database.entity.ClientDeliveryAddressEntity
import ru.mercury.vpclient.shared.mvi.Event

sealed interface FittingAddressesEvent: Event {
    data class SelectAddress(val origin: FittingAddressesOrigin, val selectedClientAddressId: Int?, val clientAddresses: List<ClientDeliveryAddressEntity>): FittingAddressesEvent
}
