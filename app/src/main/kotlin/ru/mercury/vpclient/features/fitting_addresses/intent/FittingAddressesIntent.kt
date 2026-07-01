package ru.mercury.vpclient.features.fitting_addresses.intent

import ru.mercury.vpclient.shared.data.entity.ClientDeliveryAddressSuggestion
import ru.mercury.vpclient.shared.data.entity.FittingAddressFormField
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface FittingAddressesIntent: Intent {
    data object BackClick: FittingAddressesIntent
    data object SaveAddressSelectionClick: FittingAddressesIntent
    data object AddAddressClick: FittingAddressesIntent
    data object HideAddressForm: FittingAddressesIntent
    data object OpenAddressSearch: FittingAddressesIntent
    data object HideAddressSearch: FittingAddressesIntent
    data object SaveAddressClick: FittingAddressesIntent
    data object HideAddressActions: FittingAddressesIntent
    data object EditAddressClick: FittingAddressesIntent
    data object DismissDeleteAddress: FittingAddressesIntent
    data object ConfirmDeleteAddress: FittingAddressesIntent
    data object CollectRoute: FittingAddressesIntent
    data object CollectClientAddresses: FittingAddressesIntent
    data object LoadClientAddresses: FittingAddressesIntent
    data class SelectClientAddress(val addressId: Int): FittingAddressesIntent
    data class OpenAddressActions(val addressId: Int): FittingAddressesIntent
    data class RequestDeleteAddress(val addressId: Int): FittingAddressesIntent
    data class AddressFormValueChange(val field: FittingAddressFormField, val value: String): FittingAddressesIntent
    data class SelectAddressSuggestion(val suggestion: ClientDeliveryAddressSuggestion): FittingAddressesIntent
}
