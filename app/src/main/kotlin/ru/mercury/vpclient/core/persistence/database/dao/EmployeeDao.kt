package ru.mercury.vpclient.core.persistence.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.core.persistence.database.entity.EmployeeEntity

@Dao
interface EmployeeDao {

    @Query("SELECT * FROM employee ORDER BY isActive DESC, employeeSurname ASC, employeeName ASC")
    fun selectAllFlow(): Flow<List<EmployeeEntity>>

    @Query("SELECT * FROM employee WHERE employeeId = :employeeId LIMIT 1")
    fun selectFlow(employeeId: String): Flow<EmployeeEntity>

    @Query("SELECT * FROM employee WHERE employeeId = :employeeId LIMIT 1")
    suspend fun select(employeeId: String): EmployeeEntity?

    @Upsert
    suspend fun upsert(entities: List<EmployeeEntity>)

    @Upsert
    suspend fun upsert(entity: EmployeeEntity)

    @Query("DELETE FROM employee WHERE employeeId NOT IN (:employeeIds)")
    suspend fun removeMissing(employeeIds: List<String>)

    @Query("DELETE FROM employee")
    suspend fun clear()
}
