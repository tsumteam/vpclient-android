package ru.mercury.vpclient.core.persistence.database.inject

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.mercury.vpclient.core.persistence.database.AppDatabase
import ru.mercury.vpclient.core.persistence.database.dao.BoutiqueDao
import ru.mercury.vpclient.core.persistence.database.dao.CargoDao
import ru.mercury.vpclient.core.persistence.database.dao.DeliveryDao
import ru.mercury.vpclient.core.persistence.database.dao.LoyaltyDao
import ru.mercury.vpclient.core.persistence.database.dao.OutboxDao
import ru.mercury.vpclient.core.persistence.database.dao.PaymentDao
import ru.mercury.vpclient.core.persistence.database.dao.PaymentMethodDao
import ru.mercury.vpclient.core.persistence.database.dao.ProductDao
import ru.mercury.vpclient.core.persistence.database.dao.RouteDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun appDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration(dropAllTables = true).build()
    }

    @Provides
    fun boutiqueDao(database: AppDatabase): BoutiqueDao = database.boutiqueDao()

    @Provides
    fun cargoDao(database: AppDatabase): CargoDao = database.cargoDao()

    @Provides
    fun deliveryDao(database: AppDatabase): DeliveryDao = database.deliveryDao()

    @Provides
    fun loyaltyDao(database: AppDatabase): LoyaltyDao = database.loyaltyDao()

    @Provides
    fun outboxDao(database: AppDatabase): OutboxDao = database.outboxDao()

    @Provides
    fun paymentDao(database: AppDatabase): PaymentDao = database.paymentDao()

    @Provides
    fun paymentMethodDao(database: AppDatabase): PaymentMethodDao = database.paymentMethodDao()

    @Provides
    fun productDao(database: AppDatabase): ProductDao = database.productDao()

    @Provides
    fun routeDao(database: AppDatabase): RouteDao = database.routeDao()
}
