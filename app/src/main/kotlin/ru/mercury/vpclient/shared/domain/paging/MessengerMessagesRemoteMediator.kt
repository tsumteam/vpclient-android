@file:OptIn(ExperimentalPagingApi::class)

package ru.mercury.vpclient.shared.domain.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import kotlinx.coroutines.CancellationException
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.request.MessageGetRequest
import ru.mercury.vpclient.shared.data.persistence.database.AppDatabase
import ru.mercury.vpclient.shared.data.persistence.database.dao.MessengerMessageDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.MessengerMessageEntity
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.entities
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult

class MessengerMessagesRemoteMediator(
    private val networkService: NetworkService,
    private val appDatabase: AppDatabase,
    private val messengerMessageDao: MessengerMessageDao,
    private val settingsDataStore: SettingsDataStore
): RemoteMediator<Int, MessengerMessageEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MessengerMessageEntity>
    ): MediatorResult {
        return try {
            val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
            if (pairedUserId.isEmpty()) {
                return MediatorResult.Success(endOfPaginationReached = true)
            }
            val cursor = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> messengerMessageDao.minMessageId()
                    ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
            val loadLimit = when (loadType) {
                LoadType.REFRESH -> state.config.initialLoadSize
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> state.config.pageSize
            }
            val response = handleResponseResult {
                val request = MessageGetRequest(
                    pairedUserId = pairedUserId,
                    fromMessageId = cursor,
                    limit = loadLimit,
                    toBackward = cursor != null
                )
                networkService.basketChatGet(request)
            }.getOrThrow()
            val responseItems = response.items.orEmpty()
            val entities = responseItems.entities
            val isEndOfPaginationReached = responseItems.size < loadLimit ||
                loadType == LoadType.APPEND && cursor != null &&
                responseItems.all { item -> (item.messageId ?: Long.MAX_VALUE) >= cursor }

            if (loadType == LoadType.REFRESH) {
                appDatabase.withTransaction {
                    messengerMessageDao.delete()
                    messengerMessageDao.upsert(entities)
                }
            } else {
                messengerMessageDao.upsert(entities)
            }

            MediatorResult.Success(endOfPaginationReached = isEndOfPaginationReached)
        } catch (exception: CancellationException) {
            throw exception
        } catch (throwable: Throwable) {
            MediatorResult.Error(throwable)
        }
    }
}
