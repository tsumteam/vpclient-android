package ru.mercury.vpclient.features.fitting_address_search_sheet.intent

import ru.mercury.vpclient.shared.data.entity.ClientDeliveryAddressSuggestion
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface FittingAddressSearchIntent: Intent {
    data object DismissClick: FittingAddressSearchIntent
    data object CollectInitialQuery: FittingAddressSearchIntent
    data object CollectAddressSuggestions: FittingAddressSearchIntent
    data class QueryChange(val value: String): FittingAddressSearchIntent
    data class SelectAddressSuggestion(val suggestion: ClientDeliveryAddressSuggestion): FittingAddressSearchIntent
}
