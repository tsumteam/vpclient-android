package ru.mercury.vpclient.shared.data.persistence.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.mercury.vpclient.shared.data.persistence.database.AppDatabase
import ru.mercury.vpclient.shared.data.persistence.database.dao.ActivityCounterDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CartProductDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogBrandDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogCategoryDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogFilterDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogFilterProductsDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogFilterProductsQuantityDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogViewHistoryProductDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.ClientDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.ClientDeliveryAddressDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.ClientNotificationDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CompilationDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CompilationPreviewPageDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.EmployeeBadgeDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.EmployeeDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.FavoriteBrandDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.FilterValuesDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.FilterValuesQuantityDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.FittingProductDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.GiftCardDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.LoyaltyCardInfoDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.MainScreenSectionDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.PagingKeyDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.ProductDao
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
    fun activityCounterDao(database: AppDatabase): ActivityCounterDao {
        return database.activityCounterDao()
    }

    @Provides
    fun clientDao(database: AppDatabase): ClientDao = database.clientDao()

    @Provides
    fun clientDeliveryAddressDao(database: AppDatabase): ClientDeliveryAddressDao {
        return database.clientDeliveryAddressDao()
    }

    @Provides
    fun clientNotificationDao(database: AppDatabase): ClientNotificationDao {
        return database.clientNotificationDao()
    }

    @Provides
    fun compilationDao(database: AppDatabase): CompilationDao = database.compilationDao()

    @Provides
    fun compilationPreviewPageDao(database: AppDatabase): CompilationPreviewPageDao {
        return database.compilationPreviewPageDao()
    }

    @Provides
    fun catalogCategoryDao(database: AppDatabase): CatalogCategoryDao {
        return database.catalogCategoryDao()
    }

    @Provides
    fun cartProductDao(database: AppDatabase): CartProductDao = database.cartProductDao()

    @Provides
    fun catalogBrandDao(database: AppDatabase): CatalogBrandDao = database.catalogBrandDao()

    @Provides
    fun catalogFilterDao(database: AppDatabase): CatalogFilterDao = database.catalogFilterDao()

    @Provides
    fun catalogFilterProductsDao(database: AppDatabase): CatalogFilterProductsDao {
        return database.catalogFilterProductsDao()
    }

    @Provides
    fun catalogFilterProductsQuantityDao(database: AppDatabase): CatalogFilterProductsQuantityDao {
        return database.catalogFilterProductsQuantityDao()
    }

    @Provides
    fun catalogViewHistoryProductDao(database: AppDatabase): CatalogViewHistoryProductDao {
        return database.catalogViewHistoryProductDao()
    }

    @Provides
    fun employeeDao(database: AppDatabase): EmployeeDao = database.employeeDao()

    @Provides
    fun employeeBadgeDao(database: AppDatabase): EmployeeBadgeDao = database.employeeBadgeDao()

    @Provides
    fun favoriteBrandDao(database: AppDatabase): FavoriteBrandDao = database.favoriteBrandDao()

    @Provides
    fun filterValuesDao(database: AppDatabase): FilterValuesDao = database.filterValuesDao()

    @Provides
    fun filterValuesQuantityDao(database: AppDatabase): FilterValuesQuantityDao {
        return database.filterValuesQuantityDao()
    }

    @Provides
    fun fittingProductDao(database: AppDatabase): FittingProductDao = database.fittingProductDao()

    @Provides
    fun giftCardDao(database: AppDatabase): GiftCardDao {
        return database.giftCardDao()
    }

    @Provides
    fun loyaltyCardInfoDao(database: AppDatabase): LoyaltyCardInfoDao {
        return database.loyaltyCardInfoDao()
    }

    @Provides
    fun mainScreenSectionDao(database: AppDatabase): MainScreenSectionDao {
        return database.mainScreenSectionDao()
    }

    @Provides
    fun pagingKeyDao(database: AppDatabase): PagingKeyDao = database.pagingKeyDao()

    @Provides
    fun productDao(database: AppDatabase): ProductDao = database.productDao()
}
