package ru.mercury.vpclient.shared.data.persistence.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.data.persistence.database.entity.LoyaltyCardInfoEntity

@Dao
interface LoyaltyCardInfoDao {

    @Query("SELECT * FROM LoyaltyCardInfo LIMIT 1")
    fun selectFlow(): Flow<LoyaltyCardInfoEntity?>

    @Upsert
    suspend fun upsert(entity: LoyaltyCardInfoEntity)

    @Query("DELETE FROM LoyaltyCardInfo")
    suspend fun delete()
}
