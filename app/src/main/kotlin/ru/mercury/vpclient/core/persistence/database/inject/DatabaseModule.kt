package ru.mercury.vpclient.core.persistence.database.inject

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.mercury.vpclient.core.persistence.database.AppDatabase
import ru.mercury.vpclient.core.persistence.database.dao.CatalogCategoryDao
import ru.mercury.vpclient.core.persistence.database.dao.ClientDao
import ru.mercury.vpclient.core.persistence.database.dao.EmployeeDao
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
    fun catalogCategoryDao(database: AppDatabase): CatalogCategoryDao = database.catalogCategoryDao()

    @Provides
    fun clientDao(database: AppDatabase): ClientDao = database.clientDao()

    @Provides
    fun employeeDao(database: AppDatabase): EmployeeDao = database.employeeDao()
}
