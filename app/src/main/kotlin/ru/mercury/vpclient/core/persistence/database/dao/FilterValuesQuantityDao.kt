package ru.mercury.vpclient.core.persistence.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.core.persistence.database.entity.FilterValuesQuantityEntity

@Dao
interface FilterValuesQuantityDao {

    @Query("SELECT * FROM FilterValuesQuantity WHERE chipId = :chipId LIMIT 1")
    fun selectFlow(chipId: String): Flow<FilterValuesQuantityEntity?>

    @Upsert
    suspend fun upsert(entity: FilterValuesQuantityEntity)
}
