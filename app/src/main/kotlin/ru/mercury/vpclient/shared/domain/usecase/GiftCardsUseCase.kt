package ru.mercury.vpclient.shared.domain.usecase

import androidx.room.withTransaction
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.type.GiftCardType
import ru.mercury.vpclient.shared.data.persistence.database.AppDatabase
import ru.mercury.vpclient.shared.data.persistence.database.dao.GiftCardDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.GiftCardEntity
import ru.mercury.vpclient.shared.domain.mapper.entities
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import javax.inject.Inject

class GiftCardsUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val appDatabase: AppDatabase,
    private val giftCardDao: GiftCardDao,
    dispatchers: SharedDispatchers
): UseCase<Unit, Unit>(dispatchers.io) {

    override suspend fun execute(params: Unit) {
        handleResponse(
            request = { networkService.giftCards(category = GIFT_CARD_CATEGORY_ALL) },
            onSuccess = { response ->
                val entity: GiftCardEntity = response.items.orEmpty().entities
                    .firstOrNull { entity -> entity.type == GiftCardType.VIRTUAL }
                    ?: throw GiftCardsException("Виртуальная подарочная карта не найдена")
                appDatabase.withTransaction {
                    giftCardDao.delete()
                    giftCardDao.upsert(entity)
                }
            },
            onFailure = { error -> throw GiftCardsException(error.message) }
        )
    }

    private companion object {
        private const val GIFT_CARD_CATEGORY_ALL = "all"
    }

    data class GiftCardsException(
        override val message: String
    ): ClientException(message)
}
