package ru.mercury.vpclient.shared.data.persistence.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.data.persistence.database.entity.CartProductEntity

@Dao
abstract class CartProductDao {

    @Query("SELECT * FROM CartProduct ORDER BY position ASC")
    abstract fun selectAllFlow(): Flow<List<CartProductEntity>>

    @Query("SELECT COALESCE(SUM(quantity * sizeCount), 0) FROM CartProduct")
    abstract fun cartSizeFlow(): Flow<Int>

    @Transaction
    open suspend fun replace(entities: List<CartProductEntity>) {
        clear()
        upsert(entities)
    }

    @Upsert
    abstract suspend fun upsert(entities: List<CartProductEntity>)

    @Query("UPDATE CartProduct SET isForPayment = :isForPayment WHERE id = :id")
    abstract suspend fun updateIsForPayment(id: String, isForPayment: Boolean)

    @Query("UPDATE CartProduct SET size = :size WHERE id = :id")
    abstract suspend fun updateSize(id: String, size: String)

    @Query("DELETE FROM CartProduct")
    abstract suspend fun clear()
}
