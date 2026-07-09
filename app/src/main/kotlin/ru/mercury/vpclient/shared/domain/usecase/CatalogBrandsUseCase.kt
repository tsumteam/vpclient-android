package ru.mercury.vpclient.shared.domain.usecase

import androidx.room.withTransaction
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.persistence.database.AppDatabase
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogBrandDao
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.catalogBrandEntities
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import javax.inject.Inject

class CatalogBrandsUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val appDatabase: AppDatabase,
    private val catalogBrandDao: CatalogBrandDao,
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): UseCase<Unit, Unit>(dispatchers.io) {

    override suspend fun execute(params: Unit) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()

        handleResponse(
            request = { networkService.catalogBrands() },
            onSuccess = { response ->
                val entities = response.items.orEmpty().catalogBrandEntities(pairedUserId)
                appDatabase.withTransaction {
                    catalogBrandDao.delete(pairedUserId)
                    catalogBrandDao.upsert(entities)
                }
            },
            onFailure = { error -> throw CatalogBrandsException(error.message) }
        )
    }

    data class CatalogBrandsException(
        override val message: String
    ): ClientException(message)
}
