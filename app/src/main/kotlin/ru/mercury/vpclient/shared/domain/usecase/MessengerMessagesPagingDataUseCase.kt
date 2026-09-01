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
import ru.mercury.vpclient.shared.data.persistence.database.dao.MessengerMessageDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.MessengerMessageEntity
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.paging.MessengerMessagesRemoteMediator
import javax.inject.Inject

private const val MESSENGER_PAGE_SIZE = 30

class MessengerMessagesPagingDataUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val appDatabase: AppDatabase,
    private val messengerMessageDao: MessengerMessageDao,
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): FlowUseCase<Unit, PagingData<MessengerMessageEntity>>(dispatchers.io) {

    override fun execute(parameters: Unit): Flow<PagingData<MessengerMessageEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = MESSENGER_PAGE_SIZE,
                initialLoadSize = MESSENGER_PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = MessengerMessagesRemoteMediator(
                networkService = networkService,
                appDatabase = appDatabase,
                messengerMessageDao = messengerMessageDao,
                settingsDataStore = settingsDataStore
            ),
            pagingSourceFactory = {
                messengerMessageDao.pagingSource()
            }
        ).flow
    }
}
