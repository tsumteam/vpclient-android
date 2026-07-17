package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.CatalogPagerPage
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogCategoryDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogCategoryEntity
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.isBasic
import ru.mercury.vpclient.shared.domain.mapper.isTop
import ru.mercury.vpclient.shared.domain.usecase.CatalogDataFlowUseCase.CatalogData
import ru.mercury.vpclient.shared.ui.components.catalog.CatalogTabData
import javax.inject.Inject

class CatalogDataFlowUseCase @Inject constructor(
    private val catalogCategoryDao: CatalogCategoryDao,
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): FlowUseCase<Unit, CatalogData>(dispatchers.io) {

    override fun execute(parameters: Unit): Flow<CatalogData> {
        return combine(
            catalogCategoryDao.selectAllFlow(),
            settingsDataStore.getValueFlow(PreferenceKey.LastCatalogRootId)
        ) { entities, selectedRootId ->
            val basicCategories = entities
                .filter(CatalogCategoryEntity::isBasic)
                .sortedBy(CatalogCategoryEntity::position)
            val itemsByParentId = entities
                .filter(CatalogCategoryEntity::isTop)
                .groupBy(CatalogCategoryEntity::parentId)
                .mapValues { (_, items) -> items.sortedBy(CatalogCategoryEntity::position) }
            val resolvedSelectedRootId = when {
                basicCategories.any { it.id == selectedRootId } -> selectedRootId
                else -> basicCategories.firstOrNull()?.id
            }
            CatalogData(
                tabs = basicCategories.map { basicCategory ->
                    CatalogTabData(
                        title = basicCategory.name,
                        rootId = basicCategory.id,
                        selected = basicCategory.id == resolvedSelectedRootId
                    )
                },
                pages = basicCategories.map { basicCategory ->
                    itemsByParentId[basicCategory.id].orEmpty()
                }
            )
        }
    }

    data class CatalogData(
        val tabs: List<CatalogTabData> = emptyList(),
        val pages: List<CatalogPagerPage> = emptyList()
    )
}
