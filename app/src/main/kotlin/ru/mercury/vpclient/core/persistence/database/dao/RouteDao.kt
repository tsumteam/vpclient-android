package ru.mercury.vpclient.core.persistence.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.core.RouteId
import ru.mercury.vpclient.core.persistence.database.entity.RouteEntity
import ru.mercury.vpclient.core.persistence.database.pojo.RoutePojo

@Dao
interface RouteDao {

    @Query("SELECT * FROM routes LIMIT 1")
    fun selectFlow(): Flow<RouteEntity?>

    @Transaction
    @Query("SELECT * FROM routes LIMIT 1")
    fun selectPojoFlow(): Flow<RoutePojo?>

    @Query("SELECT routeId FROM routes LIMIT 1")
    suspend fun routeId(): RouteId

    @Query("SELECT driverBarcode FROM routes LIMIT 1")
    suspend fun driverBarcode(): String

    @Query("SELECT EXISTS(SELECT 1 FROM routes LIMIT 1)")
    suspend fun isExist(): Boolean

    @Query("SELECT * FROM routes LIMIT 1")
    suspend fun select(): RouteEntity

    @Query("SELECT * FROM routes LIMIT 1")
    suspend fun selectPojo(): RoutePojo

    @Upsert
    suspend fun upsert(entity: RouteEntity)

    @Update
    suspend fun update(entity: RouteEntity)

    @Delete
    suspend fun delete(entity: RouteEntity)

    @Query("DELETE FROM routes")
    suspend fun remove()
}
