package ru.mercury.vpclient.shared.data.persistence.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.data.persistence.database.entity.ActivityCounterEntity

@Dao
interface ActivityCounterDao {

    @Query("SELECT * FROM ActivityCounter WHERE type = :type LIMIT 1")
    fun selectFlow(type: String): Flow<ActivityCounterEntity?>

    @Upsert
    suspend fun upsert(entities: List<ActivityCounterEntity>)
}
