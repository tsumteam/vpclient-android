package ru.mercury.vpclient.shared.domain.usecase

import androidx.room.withTransaction
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.FittingData
import ru.mercury.vpclient.shared.data.entity.FittingDeliveryData
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.type.FittingType
import ru.mercury.vpclient.shared.data.persistence.database.AppDatabase
import ru.mercury.vpclient.shared.data.persistence.database.dao.FittingProductDao
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.cartProduct
import ru.mercury.vpclient.shared.domain.mapper.fittingDeliveryHeader
import ru.mercury.vpclient.shared.domain.mapper.fittingEntity
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult
import javax.inject.Inject

// fixme
class LoadFittingUseCase @Inject constructor(
    private val appDatabase: AppDatabase,
    private val fittingProductDao: FittingProductDao,
    private val networkService: NetworkService,
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): UseCase<Unit, FittingData>(dispatchers.io) {

    override suspend fun execute(params: Unit): FittingData {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) {
            fittingProductDao.delete()
            return FittingData()
        }

        val fitting = handleResponseResult {
            networkService.fittingsByPairedUserId(pairedUserId)
        }.getOrThrow()
        val deliveries = fitting.deliveries.orEmpty().sortedBy { it.order ?: Int.MAX_VALUE }
        val fittingDeliveries = deliveries.map { delivery ->
            FittingDeliveryData(
                id = delivery.deliveryId.orEmpty(),
                fittingType = delivery.fittingType ?: FittingType.IN_THE_STORE,
                header = delivery.fittingDeliveryHeader,
                products = delivery.lines.orEmpty()
                    .sortedBy { it.order ?: Int.MAX_VALUE }
                    .mapNotNull { it.cartProduct }
            )
        }
        val fittingProductEntities = fittingDeliveries.flatMapIndexed { deliveryIndex, delivery ->
            delivery.products.mapIndexed { productIndex, product ->
                product.fittingEntity(
                    deliveryId = delivery.id,
                    deliveryPosition = deliveryIndex,
                    position = productIndex
                )
            }
        }
        appDatabase.withTransaction {
            fittingProductDao.delete()
            fittingProductDao.upsert(fittingProductEntities)
        }

        return FittingData(
            deliveries = fittingDeliveries
        )
    }
}
