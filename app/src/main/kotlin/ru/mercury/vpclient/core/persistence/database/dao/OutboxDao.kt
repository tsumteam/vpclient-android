package ru.mercury.vpclient.core.persistence.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.core.persistence.database.entity.OutboxEntity

@Dao
interface OutboxDao {

    @Query("SELECT COUNT(*) FROM outbox WHERE failure = 1")
    fun countFlow(): Flow<Int>

    @Query("SELECT * FROM outbox ORDER BY position ASC")
    suspend fun select(): List<OutboxEntity>

    @Query("SELECT COALESCE(MAX(position), 0) FROM outbox")
    suspend fun maxPosition(): Int

    @Query("UPDATE outbox SET failure = 1 WHERE position >= :fromPosition")
    suspend fun markFailureFromPosition(fromPosition: Int)

    @Insert
    suspend fun insert(entity: OutboxEntity)

    @Delete
    suspend fun remove(entity: OutboxEntity)
}
