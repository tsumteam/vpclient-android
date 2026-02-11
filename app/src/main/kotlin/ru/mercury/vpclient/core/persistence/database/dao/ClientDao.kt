package ru.mercury.vpclient.core.persistence.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.core.persistence.database.entity.ClientEntity

@Dao
interface ClientDao {

    @Query("SELECT * FROM client LIMIT 1")
    fun selectFlow(): Flow<ClientEntity>

    @Query("SELECT * FROM client LIMIT 1")
    suspend fun select(): ClientEntity

    @Upsert
    suspend fun upsert(entity: ClientEntity)

    @Update
    suspend fun update(entity: ClientEntity)

    @Query("DELETE FROM client")
    suspend fun remove()
}
