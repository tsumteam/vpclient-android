package ru.mercury.vpclient.shared.domain.usecase

import androidx.room.withTransaction
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.request.MessageGetRequest
import ru.mercury.vpclient.shared.data.persistence.database.AppDatabase
import ru.mercury.vpclient.shared.data.persistence.database.dao.MessengerMessageDao
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.entities
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import javax.inject.Inject

class MessengerMessagesUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val settingsDataStore: SettingsDataStore,
    private val database: AppDatabase,
    private val messengerMessageDao: MessengerMessageDao,
    dispatchers: SharedDispatchers
): UseCase<Unit, Unit>(dispatchers.io) {

    override suspend fun execute(params: Unit) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) return

        handleResponse(
            request = {
                val request = MessageGetRequest(
                    pairedUserId = pairedUserId,
                    limit = MESSAGES_LIMIT, // fixme
                    toBackward = false
                )
                networkService.basketChatGet(request)
            },
            onSuccess = { response ->
                val entities = response.items.orEmpty().entities
                database.withTransaction {
                    messengerMessageDao.delete()
                    messengerMessageDao.upsert(entities)
                }
            },
            onFailure = { error -> throw MessengerMessagesException(error.message) }
        )
    }

    data class MessengerMessagesException(
        override val message: String
    ): ClientException(message)

    private companion object {
        private const val MESSAGES_LIMIT = 50
    }
}
