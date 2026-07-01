@file:OptIn(ExperimentalPagingApi::class)

package ru.mercury.vpclient.shared.domain.usecase

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.persistence.database.AppDatabase
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogViewHistoryProductDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.PagingKeyDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.domain.paging.CatalogViewHistoryRemoteMediator
import javax.inject.Inject

private const val CATALOG_VIEW_HISTORY_PAGE_SIZE = 15

// fixme
class ViewHistoryProductsPagingDataUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val appDatabase: AppDatabase,
    private val catalogViewHistoryProductDao: CatalogViewHistoryProductDao,
    private val pagingKeyDao: PagingKeyDao,
    dispatchers: SharedDispatchers
): FlowUseCase<Unit, PagingData<CatalogFilterProductsEntity>>(dispatchers.io) {

    override fun execute(parameters: Unit): Flow<PagingData<CatalogFilterProductsEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = CATALOG_VIEW_HISTORY_PAGE_SIZE,
                initialLoadSize = CATALOG_VIEW_HISTORY_PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = CatalogViewHistoryRemoteMediator(
                networkService = networkService,
                appDatabase = appDatabase,
                catalogViewHistoryProductDao = catalogViewHistoryProductDao,
                pagingKeyDao = pagingKeyDao
            ),
            pagingSourceFactory = {
                catalogViewHistoryProductDao.pagingSource()
            }
        ).flow
    }
}
