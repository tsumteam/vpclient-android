package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.serialization.json.Json
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.TabType
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.usecase.AddSearchHistoryItemUseCase.Params
import javax.inject.Inject

class AddSearchHistoryItemUseCase @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): UseCase<Params, Unit>(dispatchers.io) {

    override suspend fun execute(params: Params) {
        val preferenceKey = when (params.tab) {
            TabType.WOMAN -> PreferenceKey.SearchHistoryWoman
            TabType.MAN -> PreferenceKey.SearchHistoryMan
            TabType.CHILD -> PreferenceKey.SearchHistoryChild
        }
        val searchHistoryJson = settingsDataStore.getValue(preferenceKey).orEmpty()
        val searchHistoryItems = runCatching {
            Json.decodeFromString<List<String>>(searchHistoryJson)
        }.getOrDefault(emptyList())
        val updatedSearchHistoryItems = searchHistoryItems
            .filterNot { item -> item.equals(params.item, ignoreCase = true) }
            .toMutableList()
            .apply { add(0, params.item) }

        settingsDataStore.setValue(
            key = preferenceKey,
            value = Json.encodeToString(updatedSearchHistoryItems)
        )
    }

    data class Params(
        val tab: TabType,
        val item: String
    )

    companion object {
        const val SEARCH_QUERY_MIN_LENGTH = 3
    }
}
