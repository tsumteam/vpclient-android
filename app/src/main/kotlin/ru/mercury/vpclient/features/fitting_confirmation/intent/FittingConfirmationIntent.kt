package ru.mercury.vpclient.features.fitting_confirmation.intent

import ru.mercury.vpclient.shared.data.entity.ClientDeliveryAddressSuggestion
import ru.mercury.vpclient.shared.data.entity.FittingAddressFormField
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryMode
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationPlaceType
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface FittingConfirmationIntent: Intent {
    data object BackClick: FittingConfirmationIntent
    data object ConfirmClick: FittingConfirmationIntent
    data object LoadFittingData: FittingConfirmationIntent
    data object LoadClientAddresses: FittingConfirmationIntent
    data object OpenAddressSelection: FittingConfirmationIntent
    data object AddressSelectionBackClick: FittingConfirmationIntent
    data object SaveAddressSelectionClick: FittingConfirmationIntent
    data object AddAddressClick: FittingConfirmationIntent
    data object HideAddressForm: FittingConfirmationIntent
    data object OpenAddressSearch: FittingConfirmationIntent
    data object HideAddressSearch: FittingConfirmationIntent
    data object SaveAddressClick: FittingConfirmationIntent
    data object HideAddressActions: FittingConfirmationIntent
    data object EditAddressClick: FittingConfirmationIntent
    data object DismissDeleteAddress: FittingConfirmationIntent
    data object ConfirmDeleteAddress: FittingConfirmationIntent
    data object CollectRoute: FittingConfirmationIntent
    data object CollectAddressSelectionRoute: FittingConfirmationIntent
    data object CollectCartProducts: FittingConfirmationIntent
    data object CollectAddressSelectionResult: FittingConfirmationIntent
    data class SelectPlace(val placeType: FittingConfirmationPlaceType): FittingConfirmationIntent
    data class SelectDeliveryMode(val mode: FittingConfirmationDeliveryMode): FittingConfirmationIntent
    data class SelectSingleDay(val dayId: String): FittingConfirmationIntent
    data class SelectSingleInterval(val intervalId: String): FittingConfirmationIntent
    data class SelectDeliveryDay(val deliveryId: String, val dayId: String): FittingConfirmationIntent
    data class SelectDeliveryInterval(val deliveryId: String, val intervalId: String): FittingConfirmationIntent
    data class ChangeDeliveryTimeClick(val deliveryId: String): FittingConfirmationIntent
    data class SelectClientAddress(val addressId: Int): FittingConfirmationIntent
    data class OpenAddressActions(val addressId: Int): FittingConfirmationIntent
    data class RequestDeleteAddress(val addressId: Int): FittingConfirmationIntent
    data class AddressFormValueChange(
        val field: FittingAddressFormField,
        val value: String
    ): FittingConfirmationIntent
    data class AddressSearchQueryChange(val query: String): FittingConfirmationIntent
    data class SelectAddressSuggestion(
        val suggestion: ClientDeliveryAddressSuggestion
    ): FittingConfirmationIntent
}
