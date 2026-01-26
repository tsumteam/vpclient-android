package ru.mercury.vpclient.core.persistence.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.mercury.vpclient.core.persistence.database.converter.Converter
import ru.mercury.vpclient.core.persistence.database.dao.BoutiqueDao
import ru.mercury.vpclient.core.persistence.database.dao.CargoDao
import ru.mercury.vpclient.core.persistence.database.dao.DeliveryDao
import ru.mercury.vpclient.core.persistence.database.dao.LoyaltyDao
import ru.mercury.vpclient.core.persistence.database.dao.OutboxDao
import ru.mercury.vpclient.core.persistence.database.dao.PaymentDao
import ru.mercury.vpclient.core.persistence.database.dao.PaymentMethodDao
import ru.mercury.vpclient.core.persistence.database.dao.ProductDao
import ru.mercury.vpclient.core.persistence.database.dao.RouteDao
import ru.mercury.vpclient.core.persistence.database.entity.BoutiqueEntity
import ru.mercury.vpclient.core.persistence.database.entity.CargoEntity
import ru.mercury.vpclient.core.persistence.database.entity.DeliveryEntity
import ru.mercury.vpclient.core.persistence.database.entity.LoyaltyEntity
import ru.mercury.vpclient.core.persistence.database.entity.OutboxEntity
import ru.mercury.vpclient.core.persistence.database.entity.PaymentEntity
import ru.mercury.vpclient.core.persistence.database.entity.PaymentMethodEntity
import ru.mercury.vpclient.core.persistence.database.entity.ProductEntity
import ru.mercury.vpclient.core.persistence.database.entity.RouteEntity

@Database(
    entities = [
        BoutiqueEntity::class,
        CargoEntity::class,
        DeliveryEntity::class,
        LoyaltyEntity::class,
        OutboxEntity::class,
        PaymentEntity::class,
        PaymentMethodEntity::class,
        ProductEntity::class,
        RouteEntity::class
    ],
    version = AppDatabase.DATABASE_VERSION,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun boutiqueDao(): BoutiqueDao
    abstract fun cargoDao(): CargoDao
    abstract fun deliveryDao(): DeliveryDao
    abstract fun loyaltyDao(): LoyaltyDao
    abstract fun outboxDao(): OutboxDao
    abstract fun paymentDao(): PaymentDao
    abstract fun paymentMethodDao(): PaymentMethodDao
    abstract fun productDao(): ProductDao
    abstract fun routeDao(): RouteDao

    companion object {
        const val DATABASE_NAME = "vpclient.db"
        const val DATABASE_VERSION = 6
    }
}
