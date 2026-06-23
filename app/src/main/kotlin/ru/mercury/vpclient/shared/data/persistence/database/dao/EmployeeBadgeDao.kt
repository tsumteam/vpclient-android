package ru.mercury.vpclient.shared.data.persistence.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeBadgeEntity

@Dao
interface EmployeeBadgeDao {

    @Upsert
    suspend fun upsert(entity: EmployeeBadgeEntity)

    @Upsert
    suspend fun upsert(entities: List<EmployeeBadgeEntity>)

    @Query("DELETE FROM EmployeeBadge WHERE employeeId NOT IN (:ids)")
    suspend fun deleteMissing(ids: List<String>)

    @Query("DELETE FROM EmployeeBadge")
    suspend fun delete()
}
