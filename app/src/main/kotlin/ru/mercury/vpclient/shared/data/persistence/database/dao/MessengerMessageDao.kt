package ru.mercury.vpclient.shared.data.persistence.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import ru.mercury.vpclient.shared.data.persistence.database.entity.MessengerMessageEntity

@Dao
interface MessengerMessageDao {

    @Query("SELECT * FROM MessengerMessage ORDER BY id DESC")
    fun pagingSource(): PagingSource<Int, MessengerMessageEntity>

    @Query("SELECT MIN(id) FROM MessengerMessage")
    suspend fun minMessageId(): Long?

    @Query("SELECT MAX(id) FROM MessengerMessage")
    suspend fun maxMessageId(): Long?

    @Query("DELETE FROM MessengerMessage")
    suspend fun delete()

    @Upsert
    suspend fun upsert(entities: List<MessengerMessageEntity>)
}
