@file:OptIn(ExperimentalCoroutinesApi::class)

package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.TabType
import ru.mercury.vpclient.shared.data.persistence.database.dao.FavoriteBrandDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.FavoriteBrandEntity
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.usecase.FavoriteBrandEntitiesFlowUseCase.FavoriteBrandEntities
import javax.inject.Inject

class FavoriteBrandEntitiesFlowUseCase @Inject constructor(
    private val favoriteBrandDao: FavoriteBrandDao,
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): FlowUseCase<Unit, FavoriteBrandEntities>(dispatchers.io) {

    override fun execute(parameters: Unit): Flow<FavoriteBrandEntities> {
        val pairedUserIdFlow = settingsDataStore.getValueFlow(PreferenceKey.PairedUser)
        return pairedUserIdFlow
            .filterNotNull()
            .flatMapLatest { pairedUserId ->
                favoriteBrandDao.selectAllFlow(pairedUserId)
                    .map { entities ->
                        FavoriteBrandEntities(
                            womanEntities = entities.filter { entity ->
                                entity.categoryId == TabType.WOMAN.value
                            },
                            manEntities = entities.filter { entity ->
                                entity.categoryId == TabType.MAN.value
                            },
                            childEntities = entities.filter { entity ->
                                entity.categoryId == TabType.CHILD.value
                            }
                        )
                    }
            }
    }

    data class FavoriteBrandEntities(
        val womanEntities: List<FavoriteBrandEntity> = emptyList(),
        val manEntities: List<FavoriteBrandEntity> = emptyList(),
        val childEntities: List<FavoriteBrandEntity> = emptyList()
    ) {

        val pages: List<List<FavoriteBrandEntity>>
            get() = listOf(womanEntities, manEntities, childEntities)
    }
}
