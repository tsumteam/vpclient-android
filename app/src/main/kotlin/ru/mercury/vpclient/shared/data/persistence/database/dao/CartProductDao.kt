package ru.mercury.vpclient.shared.data.persistence.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.data.persistence.database.entity.CartProductEntity

@Dao
interface CartProductDao {

    @Query("SELECT * FROM CartProduct ORDER BY position ASC")
    fun selectAllFlow(): Flow<List<CartProductEntity>>

    @Query("SELECT COALESCE(SUM(quantity * sizeCount), 0) FROM CartProduct")
    fun cartSizeFlow(): Flow<Int>

    @Upsert
    suspend fun upsert(entities: List<CartProductEntity>)

    @Query("UPDATE CartProduct SET isForPayment = :isForPayment WHERE id = :id")
    suspend fun updateIsForPayment(id: String, isForPayment: Boolean)

    @Query("UPDATE CartProduct SET size = :size WHERE id = :id")
    suspend fun updateSize(id: String, size: String)

    @Query("DELETE FROM CartProduct")
    suspend fun delete()
}
