package ru.mercury.vpclient.core.persistence.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.mercury.vpclient.core.persistence.database.converter.Converter
import ru.mercury.vpclient.core.persistence.database.dao.CatalogCategoryDao
import ru.mercury.vpclient.core.persistence.database.dao.ClientDao
import ru.mercury.vpclient.core.persistence.database.dao.EmployeeDao
import ru.mercury.vpclient.core.persistence.database.entity.CatalogCategoryEntity
import ru.mercury.vpclient.core.persistence.database.entity.ClientEntity
import ru.mercury.vpclient.core.persistence.database.entity.EmployeeEntity

@Database(
    entities = [
        CatalogCategoryEntity::class,
        ClientEntity::class,
        EmployeeEntity::class
    ],
    version = AppDatabase.DATABASE_VERSION,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun catalogCategoryDao(): CatalogCategoryDao
    abstract fun clientDao(): ClientDao
    abstract fun employeeDao(): EmployeeDao

    companion object {
        const val DATABASE_NAME = "vpclient.db"
        const val DATABASE_VERSION = 19
    }
}
