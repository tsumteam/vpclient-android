package ru.mercury.vpclient.core.repository.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.mercury.vpclient.core.entity.CatalogScreenData
import ru.mercury.vpclient.core.ktx.basicEntity
import ru.mercury.vpclient.core.ktx.bottomEntity
import ru.mercury.vpclient.core.ktx.childEntity
import ru.mercury.vpclient.core.ktx.handleResponse
import ru.mercury.vpclient.core.ktx.isBasic
import ru.mercury.vpclient.core.ktx.orEmpty
import ru.mercury.vpclient.core.ktx.topEntity
import ru.mercury.vpclient.core.network.NetworkService
import ru.mercury.vpclient.core.network.request.BottomCategoriesRequest
import ru.mercury.vpclient.core.persistence.database.dao.CatalogCategoryDao
import ru.mercury.vpclient.core.persistence.database.entity.CatalogCategoryEntity
import ru.mercury.vpclient.core.persistence.database.pojo.SubcategoryPojo
import ru.mercury.vpclient.core.repository.CatalogRepository
import javax.inject.Inject

class CatalogRepositoryImpl @Inject constructor(
    private val networkService: NetworkService,
    private val catalogCategoryDao: CatalogCategoryDao
): CatalogRepository {

    override fun catalogCategoryFlow(id: Int): Flow<CatalogCategoryEntity> {
        return catalogCategoryDao.selectFlow(id)
    }

    override fun subcategoryPojosFlow(parentId: Int): Flow<List<SubcategoryPojo>> {
        return catalogCategoryDao.selectPojosFlow(parentId)
    }

    override val catalogScreenDataFlow: Flow<CatalogScreenData>
        get() {
            return catalogCategoryDao.selectAllFlow().map { entities ->
                val basicCategories = entities.filter(CatalogCategoryEntity::isBasic).sortedBy(CatalogCategoryEntity::position)
                val itemsByParentId = entities
                    .filter { it.level == CatalogCategoryEntity.LEVEL_TOP }
                    .groupBy(CatalogCategoryEntity::parentId)
                    .mapValues { (_, items) -> items.sortedBy(CatalogCategoryEntity::position) }
                CatalogScreenData(
                    tabs = basicCategories.map(CatalogCategoryEntity::name),
                    pages = basicCategories.map { basicCategory -> itemsByParentId[basicCategory.id].orEmpty() }
                )
            }
        }

    override suspend fun loadCatalogCategoriesBasic() {
        handleResponse(
            request = { networkService.catalogCategoriesBasic() },
            onSuccess = { data ->
                catalogCategoryDao.upsert(
                    data.items.orEmpty().mapIndexed { index, item ->
                        item.basicEntity(position = index.plus(1))
                    }
                )
            }
        )
    }

    override suspend fun loadCatalogCategoriesTop() {
        handleResponse(
            request = { networkService.catalogCategoriesTop() },
            onSuccess = { data ->
                val categories = data.items.orEmpty()
                val entities = categories.flatMapIndexed { parentIndex, parent ->
                    buildList {
                        add(parent.basicEntity(position = parentIndex.plus(1)))
                        addAll(
                            parent.children.orEmpty().mapIndexed { childIndex, child ->
                                child.topEntity(
                                    parentId = parent.id.orEmpty,
                                    rootId = parent.id.orEmpty,
                                    position = childIndex.plus(1)
                                )
                            }
                        )
                    }
                }
                catalogCategoryDao.upsert(entities)
            }
        )
    }

    override suspend fun loadCatalogCategoriesBottom(parentCategoryId: Int) {
        handleResponse(
            request = {
                val request = BottomCategoriesRequest(parentCategoryId)
                networkService.catalogCategoriesBottom(request)
            },
            onSuccess = { data ->
                val parent = catalogCategoryDao.select(parentCategoryId)
                val rootId = parent?.rootId ?: parentCategoryId
                val bottomLevel = parent?.level?.plus(1) ?: CatalogCategoryEntity.LEVEL_BOTTOM
                val childLevel = bottomLevel.plus(1)
                val entities = data.items.orEmpty().flatMapIndexed { index, item ->
                    buildList {
                        val bottomEntity = item.bottomEntity(
                            parentId = parentCategoryId,
                            rootId = rootId,
                            position = index.plus(1)
                        ).copy(level = bottomLevel)
                        add(bottomEntity)
                        addAll(
                            item.children.orEmpty().mapIndexed { childIndex, child ->
                                child.childEntity(
                                    parentId = bottomEntity.id,
                                    rootId = rootId,
                                    level = childLevel,
                                    position = childIndex.plus(1)
                                )
                            }
                        )
                    }
                }
                catalogCategoryDao.upsert(entities)
            }
        )
    }
}
