package ru.mercury.vpclient.core.persistence.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.core.persistence.database.entity.BoutiqueEntity
import ru.mercury.vpclient.core.persistence.database.pojo.ReturnPojo
import ru.mercury.vpclient.core.BoutiqueId

@Dao
interface BoutiqueDao {

    @Transaction
    @Query("SELECT * FROM boutiques WHERE boutiqueId = :boutiqueId")
    fun selectPojoFlow(boutiqueId: BoutiqueId): Flow<ReturnPojo>

    @Query("SELECT * FROM boutiques")
    suspend fun selectAll(): List<BoutiqueEntity>

    @Query("SELECT COALESCE(MAX(position), 0) FROM boutiques")
    suspend fun maxPosition(): Int

    @Upsert
    suspend fun upsert(entities: List<BoutiqueEntity>)
}
