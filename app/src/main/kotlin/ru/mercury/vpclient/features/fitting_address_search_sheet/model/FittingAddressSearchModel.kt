package ru.mercury.vpclient.features.fitting_address_search_sheet.model

import ru.mercury.vpclient.shared.data.entity.ClientDeliveryAddressSuggestion
import ru.mercury.vpclient.shared.mvi.Model

data class FittingAddressSearchModel(
    val query: String = "",
    val suggestions: List<ClientDeliveryAddressSuggestion> = emptyList(),
    val isSuggestionsLoading: Boolean = false,
    val lastSearchQuery: String? = null
): Model
