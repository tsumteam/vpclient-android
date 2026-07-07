package ru.mercury.vpclient.shared.data.persistence.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.data.persistence.database.entity.CompilationPreviewPageEntity

@Dao
interface CompilationPreviewPageDao {

    @Query("SELECT * FROM CompilationPreviewPage WHERE compilationId = :compilationId ORDER BY position ASC")
    fun selectFlow(compilationId: Int): Flow<List<CompilationPreviewPageEntity>>

    @Upsert
    suspend fun upsert(entities: List<CompilationPreviewPageEntity>)

    @Query("DELETE FROM CompilationPreviewPage WHERE compilationId = :compilationId")
    suspend fun delete(compilationId: Int)
}
