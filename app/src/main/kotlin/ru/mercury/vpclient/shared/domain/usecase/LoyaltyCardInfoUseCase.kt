package ru.mercury.vpclient.shared.domain.usecase

import androidx.room.withTransaction
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.persistence.database.AppDatabase
import ru.mercury.vpclient.shared.data.persistence.database.dao.LoyaltyCardInfoDao
import ru.mercury.vpclient.shared.domain.mapper.entity
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import javax.inject.Inject

class LoyaltyCardInfoUseCase @Inject constructor(
    private val appDatabase: AppDatabase,
    private val networkService: NetworkService,
    private val loyaltyCardInfoDao: LoyaltyCardInfoDao,
    dispatchers: SharedDispatchers
): UseCase<Unit, Unit>(dispatchers.io) {

    override suspend fun execute(params: Unit) {
        handleResponse(
            request = { networkService.loyaltyCardInfo() },
            onSuccess = { response ->
                appDatabase.withTransaction {
                    loyaltyCardInfoDao.delete()
                    loyaltyCardInfoDao.upsert(response.entity)
                }
            },
            onEmpty = { loyaltyCardInfoDao.delete() },
            onFailure = { error -> throw LoyaltyCardInfoException(error.message) }
        )
    }

    data class LoyaltyCardInfoException(
        override val message: String
    ): ClientException(message)
}
