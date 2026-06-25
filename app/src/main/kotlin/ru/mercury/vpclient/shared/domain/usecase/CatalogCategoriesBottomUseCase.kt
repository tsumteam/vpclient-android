@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import androidx.room.withTransaction
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.error.ClientException
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.request.BottomCategoriesRequest
import ru.mercury.vpclient.shared.data.persistence.database.AppDatabase
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogCategoryDao
import ru.mercury.vpclient.shared.domain.mapper.bottomEntity
import ru.mercury.vpclient.shared.domain.mapper.childEntity
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import javax.inject.Inject

class CatalogCategoriesBottomUseCase @Inject constructor(
    private val appDatabase: AppDatabase,
    private val networkService: NetworkService,
    private val catalogCategoryDao: CatalogCategoryDao,
    dispatchers: SharedDispatchers
): UseCase<Int, Unit>(dispatchers.io) {

    override suspend fun execute(parentCategoryId: Int) {
        handleResponse(
            request = {
                val request = BottomCategoriesRequest(parentCategoryId)
                networkService.catalogCategoriesBottom(request)
            },
            onSuccess = { data ->
                val catalogCategoryResponses = data.items.orEmpty()
                val parent = catalogCategoryDao.selectNotNull(parentCategoryId)
                val rootId = parent.rootId
                val bottomLevel = parent.level.plus(1)
                val childLevel = bottomLevel.plus(1)
                val entities = catalogCategoryResponses.flatMapIndexed { index, item ->
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
                appDatabase.withTransaction {
                    catalogCategoryDao.deleteBottom(parentCategoryId)
                    catalogCategoryDao.upsert(entities)
                }
            },
            onFailure = { error -> throw CatalogCategoriesBottomException(error.message) }
        )
    }

    data class CatalogCategoriesBottomException(
        override val message: String
    ): ClientException(message)
}
