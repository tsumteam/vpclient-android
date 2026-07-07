@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import androidx.room.withTransaction
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.response.LooksResponseItemDtoResponse
import ru.mercury.vpclient.shared.data.persistence.database.AppDatabase
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogFilterProductsDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CompilationPreviewPageDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.CompilationPreviewPageEntity
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import ru.mercury.vpclient.shared.domain.mapper.pageEntity
import ru.mercury.vpclient.shared.domain.mapper.productEntities
import javax.inject.Inject

class CompilationsClientByIdUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val appDatabase: AppDatabase,
    private val compilationPreviewPageDao: CompilationPreviewPageDao,
    private val catalogFilterProductsDao: CatalogFilterProductsDao,
    dispatchers: SharedDispatchers
): UseCase<Int, Unit>(dispatchers.io) {

    override suspend fun execute(compilationId: Int) {
        handleResponse(
            request = { networkService.compilationsClientByCompilationId(compilationId) },
            onSuccess = { response ->
                val compilationName = response.compilationInfo?.name.orEmpty()
                val loadedEntities = response.looks.orEmpty().mapIndexedNotNull { index, look ->
                    loadEntities(
                        compilationId = compilationId,
                        compilationName = compilationName,
                        position = index,
                        look = look
                    )
                }
                val pageEntities = loadedEntities.map { entity -> entity.pageEntity }
                val productEntities = loadedEntities.flatMap { entity -> entity.productEntities }

                appDatabase.withTransaction {
                    compilationPreviewPageDao.delete(compilationId)
                    catalogFilterProductsDao.remove(compilationId)
                    compilationPreviewPageDao.upsert(pageEntities)
                    catalogFilterProductsDao.upsert(productEntities)
                }
            },
            onFailure = { error -> throw CompilationsClientByIdException(error.message) }
        )
    }

    private suspend fun loadEntities(
        compilationId: Int,
        compilationName: String,
        position: Int,
        look: LooksResponseItemDtoResponse
    ): LoadedCompilationEntities? {
        val lookId = look.id ?: return null
        var loadedEntities: LoadedCompilationEntities? = null
        handleResponse(
            request = { networkService.compilationsClientLookByLookId(lookId) },
            onSuccess = { response ->
                val pageEntity = response.pageEntity(
                    compilationId = compilationId,
                    compilationName = compilationName,
                    lookId = lookId,
                    position = position,
                    fallback = look
                )
                val productEntities = response.productEntities(
                    compilationId = compilationId,
                    lookId = lookId
                )
                loadedEntities = LoadedCompilationEntities(
                    pageEntity = pageEntity,
                    productEntities = productEntities
                )
            }
        )
        return loadedEntities
    }

    private data class LoadedCompilationEntities(
        val pageEntity: CompilationPreviewPageEntity,
        val productEntities: List<CatalogFilterProductsEntity>
    )

    data class CompilationsClientByIdException(
        override val message: String
    ): ClientException(message)
}
