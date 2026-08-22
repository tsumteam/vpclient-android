package ru.mercury.vpclient.features.fitting_addresses.intent

import ru.mercury.vpclient.features.fitting_address_actions_sheet.intent.FittingAddressActionsIntent
import ru.mercury.vpclient.features.fitting_address_delete_dialog.intent.FittingAddressDeleteIntent
import ru.mercury.vpclient.features.fitting_address_sheet.intent.FittingAddressIntent
import ru.mercury.vpclient.shared.data.entity.ClientDeliveryAddressSuggestion
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface FittingAddressesIntent: Intent {
    data object BackClick: FittingAddressesIntent
    data object SaveAddressSelectionClick: FittingAddressesIntent
    data object AddAddressClick: FittingAddressesIntent
    data object DismissFittingAddressSearchSheet: FittingAddressesIntent
    data object CollectRoute: FittingAddressesIntent
    data object CollectClientAddresses: FittingAddressesIntent
    data object LoadClientAddresses: FittingAddressesIntent
    data class SelectClientAddress(val addressId: Int): FittingAddressesIntent
    data class OpenAddressActions(val addressId: Int): FittingAddressesIntent
    data class SelectAddressSuggestion(val suggestion: ClientDeliveryAddressSuggestion): FittingAddressesIntent
    data class OnFittingAddressActionsIntent(val intent: FittingAddressActionsIntent): FittingAddressesIntent
    data class OnFittingAddressIntent(val intent: FittingAddressIntent): FittingAddressesIntent
    data class OnFittingAddressDeleteIntent(val intent: FittingAddressDeleteIntent): FittingAddressesIntent
}
