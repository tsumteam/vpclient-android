package ru.mercury.vpclient.shared.data.persistence.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.data.persistence.database.entity.CompilationEntity

@Dao
interface CompilationDao {

    @Query("SELECT * FROM Compilation ORDER BY position ASC")
    fun selectAllFlow(): Flow<List<CompilationEntity>>

    @Upsert
    suspend fun upsert(entities: List<CompilationEntity>)

    @Query("DELETE FROM Compilation")
    suspend fun delete()
}
