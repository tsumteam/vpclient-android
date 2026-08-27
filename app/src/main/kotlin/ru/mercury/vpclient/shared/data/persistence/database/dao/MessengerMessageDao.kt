package ru.mercury.vpclient.shared.data.persistence.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.data.persistence.database.entity.MessengerMessageEntity

@Dao
interface MessengerMessageDao {

    @Query("SELECT * FROM MessengerMessage ORDER BY id ASC")
    fun selectFlow(): Flow<List<MessengerMessageEntity>>

    @Query("DELETE FROM MessengerMessage")
    suspend fun delete()

    @Upsert
    suspend fun upsert(entities: List<MessengerMessageEntity>)
}
