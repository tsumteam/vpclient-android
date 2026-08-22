package ru.mercury.vpclient.features.fitting_confirmation.intent

import ru.mercury.vpclient.features.fitting_address_actions_sheet.intent.FittingAddressActionsIntent
import ru.mercury.vpclient.features.fitting_address_delete_dialog.intent.FittingAddressDeleteIntent
import ru.mercury.vpclient.features.fitting_address_sheet.intent.FittingAddressIntent
import ru.mercury.vpclient.features.fitting_addresses.event.FittingAddressesEvent
import ru.mercury.vpclient.shared.data.entity.ClientDeliveryAddressSuggestion
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryMode
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationPlaceType
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface FittingConfirmationIntent: Intent {
    data object BackClick: FittingConfirmationIntent
    data object ConfirmClick: FittingConfirmationIntent
    data object LoadFittingData: FittingConfirmationIntent
    data object LoadClientAddresses: FittingConfirmationIntent
    data object OpenAddressSelection: FittingConfirmationIntent
    data object AddAddressClick: FittingConfirmationIntent
    data object DismissFittingAddressSearchSheet: FittingConfirmationIntent
    data object CollectRoute: FittingConfirmationIntent
    data object CollectCartProducts: FittingConfirmationIntent
    data object CollectClientAddresses: FittingConfirmationIntent
    data class ReceiveFittingAddressesEvent(val event: FittingAddressesEvent): FittingConfirmationIntent
    data class SelectPlace(val placeType: FittingConfirmationPlaceType): FittingConfirmationIntent
    data class SelectDeliveryMode(val mode: FittingConfirmationDeliveryMode): FittingConfirmationIntent
    data class SelectSingleDay(val dayId: String): FittingConfirmationIntent
    data class SelectSingleInterval(val intervalId: String): FittingConfirmationIntent
    data class SelectDeliveryDay(val deliveryId: String, val dayId: String): FittingConfirmationIntent
    data class SelectDeliveryInterval(val deliveryId: String, val intervalId: String): FittingConfirmationIntent
    data class ChangeDeliveryTimeClick(val deliveryId: String): FittingConfirmationIntent
    data class SelectClientAddress(val addressId: Int): FittingConfirmationIntent
    data class OpenAddressActions(val addressId: Int): FittingConfirmationIntent
    data class SelectAddressSuggestion(val suggestion: ClientDeliveryAddressSuggestion): FittingConfirmationIntent
    data class OnFittingAddressActionsIntent(val intent: FittingAddressActionsIntent): FittingConfirmationIntent
    data class OnFittingAddressIntent(val intent: FittingAddressIntent): FittingConfirmationIntent
    data class OnFittingAddressDeleteIntent(val intent: FittingAddressDeleteIntent): FittingConfirmationIntent
}
