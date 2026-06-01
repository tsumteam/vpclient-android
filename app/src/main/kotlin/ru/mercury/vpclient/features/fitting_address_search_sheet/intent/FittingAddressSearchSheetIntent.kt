package ru.mercury.vpclient.features.fitting_address_search_sheet.intent

import ru.mercury.vpclient.shared.data.entity.ClientDeliveryAddressSuggestion
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface FittingAddressSearchSheetIntent: Intent {
    data object DismissRequest: FittingAddressSearchSheetIntent
    data class QueryChange(val value: String): FittingAddressSearchSheetIntent
    data class SelectAddressSuggestion(
        val suggestion: ClientDeliveryAddressSuggestion
    ): FittingAddressSearchSheetIntent
}
