@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.TabType
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import javax.inject.Inject

class SearchHistoryItemsFlowUseCase @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): FlowUseCase<TabType, List<String>>(dispatchers.io) {

    override fun execute(tab: TabType): Flow<List<String>> {
        val preferenceKey = when (tab) {
            TabType.WOMAN -> PreferenceKey.SearchHistoryWoman
            TabType.MAN -> PreferenceKey.SearchHistoryMan
            TabType.CHILD -> PreferenceKey.SearchHistoryChild
        }
        return settingsDataStore.getValueFlow(preferenceKey)
            .map { value ->
                value
                    ?.takeIf(String::isNotBlank)
                    ?.let { searchHistoryJson ->
                        runCatching {
                            Json.decodeFromString<List<String>>(searchHistoryJson)
                        }.getOrDefault(emptyList())
                    }
                    .orEmpty()
            }
    }
}
