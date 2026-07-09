package ru.mercury.vpclient.shared.data.persistence.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.data.persistence.database.entity.FavoriteBrandEntity

@Dao
interface FavoriteBrandDao {

    @Query("SELECT * FROM FavoriteBrand WHERE pairedUserId = :pairedUserId ORDER BY categoryId, position")
    fun selectAllFlow(pairedUserId: String): Flow<List<FavoriteBrandEntity>>

    @Query("SELECT * FROM FavoriteBrand WHERE pairedUserId = :pairedUserId AND categoryId = :categoryId ORDER BY position")
    fun selectByCategoryFlow(pairedUserId: String, categoryId: Int): Flow<List<FavoriteBrandEntity>>

    @Query("SELECT * FROM FavoriteBrand WHERE pairedUserId = :pairedUserId ORDER BY categoryId, position")
    suspend fun selectAll(pairedUserId: String): List<FavoriteBrandEntity>

    @Query("SELECT * FROM FavoriteBrand WHERE pairedUserId = :pairedUserId AND categoryId = :categoryId AND brandId = :brandId")
    suspend fun selectNotNull(pairedUserId: String, categoryId: Int, brandId: Int): FavoriteBrandEntity

    @Upsert
    suspend fun upsert(entities: List<FavoriteBrandEntity>)

    @Upsert
    suspend fun upsert(entity: FavoriteBrandEntity)

    @Query("DELETE FROM FavoriteBrand WHERE pairedUserId = :pairedUserId")
    suspend fun delete(pairedUserId: String)

    @Query("DELETE FROM FavoriteBrand WHERE pairedUserId = :pairedUserId AND categoryId = :categoryId AND brandId = :brandId")
    suspend fun delete(pairedUserId: String, categoryId: Int, brandId: Int)
}
