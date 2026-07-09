@file:OptIn(ExperimentalCoroutinesApi::class)
@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogBrandDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogBrandEntity
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import javax.inject.Inject

class CatalogBrandEntitiesFlowUseCase @Inject constructor(
    private val catalogBrandDao: CatalogBrandDao,
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): FlowUseCase<Int, List<CatalogBrandEntity>>(dispatchers.io) {

    override fun execute(categoryId: Int): Flow<List<CatalogBrandEntity>> {
        val pairedUserIdFlow = settingsDataStore.getValueFlow(PreferenceKey.PairedUser)
        return pairedUserIdFlow
            .filterNotNull()
            .flatMapLatest { pairedUserId ->
                catalogBrandDao.selectByCategoryFlow(pairedUserId, categoryId)
            }
    }
}
