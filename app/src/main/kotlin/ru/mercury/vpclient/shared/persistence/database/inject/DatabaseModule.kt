package ru.mercury.vpclient.shared.persistence.database.inject

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.mercury.vpclient.shared.persistence.database.AppDatabase
import ru.mercury.vpclient.shared.persistence.database.dao.CatalogCategoryDao
import ru.mercury.vpclient.shared.persistence.database.dao.CatalogFilterDao
import ru.mercury.vpclient.shared.persistence.database.dao.CatalogFilterProductsDao
import ru.mercury.vpclient.shared.persistence.database.dao.CatalogFilterProductsQuantityDao
import ru.mercury.vpclient.shared.persistence.database.dao.ClientDao
import ru.mercury.vpclient.shared.persistence.database.dao.EmployeeDao
import ru.mercury.vpclient.shared.persistence.database.dao.FilterValuesDao
import ru.mercury.vpclient.shared.persistence.database.dao.FilterValuesQuantityDao
import ru.mercury.vpclient.shared.persistence.database.dao.PagingKeyDao
import ru.mercury.vpclient.shared.persistence.database.dao.ProductDao
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
    fun catalogFilterDao(database: AppDatabase): CatalogFilterDao = database.catalogFilterDao()

    @Provides
    fun catalogFilterProductsDao(database: AppDatabase): CatalogFilterProductsDao = database.catalogFilterProductsDao()

    @Provides
    fun catalogFilterProductsQuantityDao(database: AppDatabase): CatalogFilterProductsQuantityDao = database.catalogFilterProductsQuantityDao()

    @Provides
    fun filterValuesDao(database: AppDatabase): FilterValuesDao = database.filterValuesDao()

    @Provides
    fun filterValuesQuantityDao(database: AppDatabase): FilterValuesQuantityDao = database.filterValuesQuantityDao()

    @Provides
    fun catalogCategoryDao(database: AppDatabase): CatalogCategoryDao = database.catalogCategoryDao()

    @Provides
    fun clientDao(database: AppDatabase): ClientDao = database.clientDao()

    @Provides
    fun employeeDao(database: AppDatabase): EmployeeDao = database.employeeDao()

    @Provides
    fun pagingKeyDao(database: AppDatabase): PagingKeyDao = database.pagingKeyDao()

    @Provides
    fun productDao(database: AppDatabase): ProductDao = database.productDao()
}
