package ru.mercury.vpclient.shared.domain.usecase

import androidx.room.withTransaction
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.persistence.database.AppDatabase
import ru.mercury.vpclient.shared.data.persistence.database.dao.CompilationDao
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.entity
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import javax.inject.Inject

class CompilationsClientUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val appDatabase: AppDatabase,
    private val compilationDao: CompilationDao,
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): UseCase<Unit, Unit>(dispatchers.io) {

    override suspend fun execute(params: Unit) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()

        handleResponse(
            request = {
                networkService.compilationsClient(
                    pairedUserId = pairedUserId
                )
            },
            onSuccess = { response ->
                val entities = response.items.orEmpty().mapIndexed { index, item ->
                    item.entity(position = index)
                }
                appDatabase.withTransaction {
                    compilationDao.delete()
                    compilationDao.upsert(entities)
                }
            },
            onFailure = { error -> throw CompilationsClientException(error.message) }
        )
    }

    data class CompilationsClientException(
        override val message: String
    ): ClientException(message)
}
