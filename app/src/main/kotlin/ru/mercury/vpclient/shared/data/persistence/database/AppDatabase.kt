package ru.mercury.vpclient.shared.data.persistence.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.mercury.vpclient.shared.data.persistence.database.converter.Converter
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogCategoryDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogFilterDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogFilterProductsDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogFilterProductsQuantityDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.ClientDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.EmployeeDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.FilterValuesDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.FilterValuesQuantityDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.PagingKeyDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.ProductDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogCategoryEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsQuantityEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ClientEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesQuantityEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.PagingKeyEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductEntity

@Database(
    entities = [
        CatalogFilterEntity::class,
        CatalogFilterProductsEntity::class,
        CatalogFilterProductsQuantityEntity::class,
        FilterValuesEntity::class,
        FilterValuesQuantityEntity::class,
        CatalogCategoryEntity::class,
        ClientEntity::class,
        EmployeeEntity::class,
        PagingKeyEntity::class,
        ProductEntity::class
    ],
    version = AppDatabase.DATABASE_VERSION,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun catalogFilterDao(): CatalogFilterDao
    abstract fun catalogFilterProductsDao(): CatalogFilterProductsDao
    abstract fun catalogFilterProductsQuantityDao(): CatalogFilterProductsQuantityDao
    abstract fun filterValuesDao(): FilterValuesDao
    abstract fun filterValuesQuantityDao(): FilterValuesQuantityDao
    abstract fun catalogCategoryDao(): CatalogCategoryDao
    abstract fun clientDao(): ClientDao
    abstract fun employeeDao(): EmployeeDao
    abstract fun pagingKeyDao(): PagingKeyDao
    abstract fun productDao(): ProductDao

    companion object {
        const val DATABASE_NAME = "vpclient.db"
        const val DATABASE_VERSION = 41
    }
}
