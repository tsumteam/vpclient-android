package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.TabType
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import javax.inject.Inject

class SelectedTabFlowUseCase @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): FlowUseCase<Unit, TabType>(dispatchers.io) {

    override fun execute(parameters: Unit): Flow<TabType> {
        return settingsDataStore.getValueFlow(PreferenceKey.LastCatalogRootId)
            .map { rootId ->
                when (rootId) {
                    3 -> TabType.MAN
                    4 -> TabType.CHILD
                    else -> TabType.WOMAN
                }
            }
    }
}
