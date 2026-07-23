@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.request.CatalogByTextSuggestsDigineticaRequest
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import javax.inject.Inject

class CatalogByTextSuggestsDigineticaUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val settingsDataStore: SettingsDataStore,
    private val currentUserUseCase: CurrentUserUseCase,
    dispatchers: SharedDispatchers
): UseCase<String, List<String>>(dispatchers.io) {

    override suspend fun execute(searchText: String): List<String> {
        var suggestions = emptyList<String>()
        val useDiginetica = settingsDataStore.getValue(PreferenceKey.UseDiginetica)
            ?: runCatching { currentUserUseCase(Unit).getOrThrow().useDiginetica == true }
                .getOrElse { throwable ->
                    throw CatalogByTextSuggestsDigineticaException(
                        searchText = searchText,
                        message = throwable.message.orEmpty()
                    )
                }

        handleResponse(
            request = {
                val request = CatalogByTextSuggestsDigineticaRequest(
                    searchText = searchText
                )
                when {
                    useDiginetica -> {
                        networkService.catalogByTextSuggestsDiginetica(
                            limit = SEARCH_SUGGESTIONS_LIMIT,
                            request = request
                        )
                    }
                    else -> {
                        networkService.catalogByTextSuggests(
                            limit = SEARCH_SUGGESTIONS_LIMIT,
                            request = request
                        )
                    }
                }
            },
            onSuccess = { response -> suggestions = response.items.orEmpty() },
            onFailure = { error ->
                throw CatalogByTextSuggestsDigineticaException(
                    searchText = searchText,
                    message = error.message
                )
            }
        )

        return suggestions
    }

    data class CatalogByTextSuggestsDigineticaException(
        val searchText: String,
        override val message: String
    ): ClientException(message)

    companion object {
        const val SEARCH_SUGGESTIONS_DEBOUNCE_MILLIS = 750L
        private const val SEARCH_SUGGESTIONS_LIMIT = 10
    }
}
