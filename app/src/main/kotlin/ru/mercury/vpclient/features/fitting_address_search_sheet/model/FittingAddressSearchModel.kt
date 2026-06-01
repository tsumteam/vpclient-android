package ru.mercury.vpclient.features.fitting_address_search_sheet.model

import ru.mercury.vpclient.shared.data.entity.ClientDeliveryAddressSuggestion

data class FittingAddressSearchModel(
    val query: String = "",
    val suggestions: List<ClientDeliveryAddressSuggestion> = emptyList(),
    val isSuggestionsLoading: Boolean = false
)
