package ru.mercury.vpclient.shared.data.persistence.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesEntity

@Dao
interface FilterValuesDao {

    @Query("SELECT * FROM FilterValues WHERE chipId = :chipId LIMIT 1")
    fun selectFlow(chipId: String): Flow<FilterValuesEntity?>

    @Query("SELECT * FROM FilterValues WHERE chipId = :chipId LIMIT 1")
    suspend fun select(chipId: String): FilterValuesEntity?

    @Upsert
    suspend fun upsert(entity: FilterValuesEntity)
}
