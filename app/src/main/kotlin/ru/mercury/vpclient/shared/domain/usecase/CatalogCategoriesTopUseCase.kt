package ru.mercury.vpclient.shared.domain.usecase

import androidx.room.withTransaction
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.persistence.database.AppDatabase
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogCategoryDao
import ru.mercury.vpclient.shared.domain.mapper.basicEntity
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import ru.mercury.vpclient.shared.domain.mapper.orEmpty
import ru.mercury.vpclient.shared.domain.mapper.topEntity
import javax.inject.Inject

class CatalogCategoriesTopUseCase @Inject constructor(
    private val appDatabase: AppDatabase,
    private val networkService: NetworkService,
    private val catalogCategoryDao: CatalogCategoryDao,
    dispatchers: SharedDispatchers
): UseCase<Unit, Unit>(dispatchers.io) {

    override suspend fun execute(params: Unit) {
        handleResponse(
            request = { networkService.catalogCategoriesTop() },
            onSuccess = { data ->
                val catalogCategoryResponses = data.items.orEmpty()
                val entities = catalogCategoryResponses.flatMapIndexed { parentIndex, catalogCategoryResponse ->
                    buildList {
                        val rootId = catalogCategoryResponse.id.orEmpty
                        val parentCatalogCategoryEntity = catalogCategoryResponse.basicEntity(
                            position = parentIndex.plus(1)
                        )
                        add(parentCatalogCategoryEntity)
                        addAll(
                            catalogCategoryResponse.children.orEmpty().mapIndexed { childIndex, child ->
                                child.topEntity(
                                    parentId = rootId,
                                    rootId = rootId,
                                    position = childIndex.plus(1)
                                )
                            }
                        )
                    }
                }
                appDatabase.withTransaction {
                    catalogCategoryDao.delete()
                    catalogCategoryDao.upsert(entities)
                }
            },
            onFailure = { error -> throw CatalogCategoriesTopException(error.message) }
        )
    }

    data class CatalogCategoriesTopException(
        override val message: String
    ): ClientException(message)
}
