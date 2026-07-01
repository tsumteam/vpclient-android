package ru.mercury.vpclient.shared.domain.usecase

import androidx.room.withTransaction
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.persistence.database.AppDatabase
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogViewHistoryProductDao
import ru.mercury.vpclient.shared.domain.mapper.entity
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import javax.inject.Inject

class CatalogViewHistoryUseCase @Inject constructor(
    private val appDatabase: AppDatabase,
    private val networkService: NetworkService,
    private val catalogViewHistoryProductDao: CatalogViewHistoryProductDao,
    dispatchers: SharedDispatchers
): UseCase<Unit, Unit>(dispatchers.io) {

    override suspend fun execute(params: Unit) {
        handleResponse(
            request = {
                networkService.catalogViewHistory(
                    limit = PROFILE_VIEW_HISTORY_LIMIT
                )
            },
            onSuccess = { response ->
                val entities = response.items.orEmpty().mapIndexed { index, item ->
                    item.entity(
                        position = index
                    )
                }
                appDatabase.withTransaction {
                    catalogViewHistoryProductDao.delete()
                    catalogViewHistoryProductDao.upsert(entities)
                }
            },
            onFailure = { error -> throw CatalogViewHistoryException(error.message) }
        )
    }

    data class CatalogViewHistoryException(
        override val message: String
    ): ClientException(message)

    private companion object {
        private const val PROFILE_VIEW_HISTORY_LIMIT = 11
    }
}
