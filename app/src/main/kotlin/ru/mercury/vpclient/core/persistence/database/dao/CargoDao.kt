package ru.mercury.vpclient.core.persistence.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.core.BoutiqueId
import ru.mercury.vpclient.core.DeliveryId
import ru.mercury.vpclient.core.persistence.database.entity.CargoEntity

@Dao
interface CargoDao {

    @Query("SELECT * FROM cargoPackages WHERE boutiqueId = :boutiqueId")
    fun boutiqueCargoEntitiesFlow(boutiqueId: BoutiqueId): Flow<List<CargoEntity>>

    @Query("SELECT * FROM cargoPackages WHERE deliveryId = :deliveryId")
    fun deliveryCargoEntitiesFlow(deliveryId: DeliveryId): Flow<List<CargoEntity>>

    @Query("SELECT * FROM cargoPackages")
    suspend fun selectAll(): List<CargoEntity>

    @Query("SELECT * FROM cargoPackages WHERE deliveryId = :deliveryId")
    suspend fun selectAll(deliveryId: DeliveryId): List<CargoEntity>

    @Query("SELECT * FROM cargoPackages WHERE boutiqueId = :boutiqueId AND barcode = :barcode")
    suspend fun findByBoutiqueId(boutiqueId: BoutiqueId, barcode: String): CargoEntity?

    @Query("SELECT * FROM cargoPackages WHERE deliveryId = :deliveryId AND barcode = :barcode")
    suspend fun findByDeliveryId(deliveryId: DeliveryId, barcode: String): CargoEntity?

    @Query("SELECT NOT EXISTS(SELECT 1 FROM cargoPackages WHERE boutiqueId = :boutiqueId AND scannedInBoutique = 0)")
    suspend fun isBoutiqueCargoScanned(boutiqueId: BoutiqueId): Boolean

    @Query("SELECT NOT EXISTS(SELECT 1 FROM cargoPackages WHERE deliveryId = :deliveryId AND scannedAtDelivery = 0)")
    suspend fun isDeliveryCargoScanned(deliveryId: DeliveryId): Boolean

    @Query("SELECT scannedInBoutique FROM cargoPackages WHERE barcode = :barcode LIMIT 1")
    suspend fun isCargoScannedInBoutique(barcode: String): Boolean

    @Query("SELECT scannedAtDelivery FROM cargoPackages WHERE barcode = :barcode LIMIT 1")
    suspend fun isCargoScannedAtDelivery(barcode: String): Boolean

    @Upsert
    suspend fun upsert(entities: List<CargoEntity>)

    @Update
    suspend fun update(entity: CargoEntity)

    @Query("UPDATE cargoPackages SET scannedInBoutique = 1 WHERE barcode = :barcode")
    suspend fun markScannedInBoutiqueByBarcode(barcode: String)
}
