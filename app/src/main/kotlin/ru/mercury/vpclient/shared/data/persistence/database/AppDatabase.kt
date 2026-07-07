package ru.mercury.vpclient.shared.data.persistence.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.mercury.vpclient.shared.data.persistence.database.converter.Converter
import ru.mercury.vpclient.shared.data.persistence.database.dao.ActivityCounterDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CartProductDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogCategoryDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogFilterDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogFilterProductsDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogFilterProductsQuantityDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogViewHistoryProductDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.ClientDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.ClientDeliveryAddressDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CompilationDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CompilationPreviewPageDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.EmployeeBadgeDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.EmployeeDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.FilterValuesDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.FilterValuesQuantityDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.FittingProductDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.LoyaltyCardInfoDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.PagingKeyDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.ProductDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.ActivityCounterEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.CartProductEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogCategoryEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsQuantityEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogViewHistoryProductEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ClientDeliveryAddressEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ClientEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.CompilationEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.CompilationPreviewPageEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeBadgeEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesQuantityEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.FittingProductEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.LoyaltyCardInfoEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.PagingKeyEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductEntity

@Database(
    entities = [
        ActivityCounterEntity::class,
        CartProductEntity::class,
        CatalogFilterEntity::class,
        CatalogFilterProductsEntity::class,
        CatalogFilterProductsQuantityEntity::class,
        CatalogViewHistoryProductEntity::class,
        FilterValuesEntity::class,
        FilterValuesQuantityEntity::class,
        CatalogCategoryEntity::class,
        ClientDeliveryAddressEntity::class,
        ClientEntity::class,
        CompilationEntity::class,
        CompilationPreviewPageEntity::class,
        EmployeeEntity::class,
        EmployeeBadgeEntity::class,
        FittingProductEntity::class,
        LoyaltyCardInfoEntity::class,
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
    abstract fun catalogViewHistoryProductDao(): CatalogViewHistoryProductDao
    abstract fun filterValuesDao(): FilterValuesDao
    abstract fun filterValuesQuantityDao(): FilterValuesQuantityDao
    abstract fun catalogCategoryDao(): CatalogCategoryDao
    abstract fun clientDao(): ClientDao
    abstract fun clientDeliveryAddressDao(): ClientDeliveryAddressDao
    abstract fun compilationDao(): CompilationDao
    abstract fun compilationPreviewPageDao(): CompilationPreviewPageDao
    abstract fun employeeDao(): EmployeeDao
    abstract fun employeeBadgeDao(): EmployeeBadgeDao
    abstract fun pagingKeyDao(): PagingKeyDao
    abstract fun productDao(): ProductDao
    abstract fun cartProductDao(): CartProductDao
    abstract fun fittingProductDao(): FittingProductDao
    abstract fun activityCounterDao(): ActivityCounterDao
    abstract fun loyaltyCardInfoDao(): LoyaltyCardInfoDao

    companion object {
        const val DATABASE_NAME = "vpclient.db"
        const val DATABASE_VERSION = 79
    }
}
