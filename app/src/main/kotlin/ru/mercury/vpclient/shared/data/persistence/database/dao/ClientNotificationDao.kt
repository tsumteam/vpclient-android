package ru.mercury.vpclient.shared.data.persistence.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.data.entity.ClientNotificationCategory
import ru.mercury.vpclient.shared.data.persistence.database.entity.ClientNotificationEntity

@Dao
interface ClientNotificationDao {

    @Query("SELECT * FROM ClientNotification WHERE category = :category ORDER BY timestamp DESC")
    fun selectFlow(category: ClientNotificationCategory): Flow<List<ClientNotificationEntity>>

    @Query("DELETE FROM ClientNotification WHERE category = :category")
    suspend fun delete(category: ClientNotificationCategory)

    @Upsert
    suspend fun upsert(entities: List<ClientNotificationEntity>)
}
