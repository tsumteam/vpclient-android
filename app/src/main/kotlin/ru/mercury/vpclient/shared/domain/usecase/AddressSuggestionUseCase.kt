@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.ClientDeliveryAddressSuggestion
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.domain.mapper.clientDeliveryAddressSuggestion
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult
import javax.inject.Inject

class AddressSuggestionUseCase @Inject constructor(
    private val networkService: NetworkService,
    dispatchers: SharedDispatchers
): UseCase<String, List<ClientDeliveryAddressSuggestion>>(dispatchers.io) {

    override suspend fun execute(query: String): List<ClientDeliveryAddressSuggestion> {
        return when {
            query.isBlank() -> emptyList()
            else -> {
                val result = handleResponseResult {
                    networkService.addressSuggestion(
                        limit = ADDRESS_SUGGESTION_LIMIT,
                        searchText = query
                    )
                }.getOrThrow()
                result.items.orEmpty().mapNotNull { suggestion ->
                    suggestion.clientDeliveryAddressSuggestion
                }
            }
        }
    }

    companion object {
        const val ADDRESS_SEARCH_DEBOUNCE_MS = 350L
        private const val ADDRESS_SUGGESTION_LIMIT = 10
    }
}
