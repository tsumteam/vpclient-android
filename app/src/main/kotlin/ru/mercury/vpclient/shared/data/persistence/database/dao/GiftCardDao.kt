package ru.mercury.vpclient.shared.data.persistence.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.data.network.type.GiftCardType
import ru.mercury.vpclient.shared.data.persistence.database.entity.GiftCardEntity

@Dao
interface GiftCardDao {

    @Query("SELECT * FROM GiftCard WHERE type = :type LIMIT 1")
    fun selectFlow(type: GiftCardType): Flow<GiftCardEntity?>

    @Query("DELETE FROM GiftCard")
    suspend fun delete()

    @Upsert
    suspend fun upsert(entity: GiftCardEntity)
}
