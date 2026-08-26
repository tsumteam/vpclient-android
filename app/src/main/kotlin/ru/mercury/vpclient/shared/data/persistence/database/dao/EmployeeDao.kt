package ru.mercury.vpclient.shared.data.persistence.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.data.persistence.database.pojo.ActiveEmployeePojo
import ru.mercury.vpclient.shared.data.persistence.database.pojo.EmployeePojo

@Dao
interface EmployeeDao {

    @Query("SELECT * FROM employee ORDER BY position ASC")
    fun selectAllFlow(): Flow<List<EmployeeEntity>>

    @Transaction
    @Query("SELECT * FROM employee ORDER BY position ASC")
    fun selectAllPojosFlow(): Flow<List<EmployeePojo>>

    @Query("SELECT * FROM employee WHERE isActive = 1 LIMIT 1")
    fun selectActiveFlow(): Flow<EmployeeEntity?>

    @Transaction
    @Query(
        """
        SELECT Employee.*,
               COALESCE(
                   (SELECT value FROM ActivityCounter WHERE type = :messengerType LIMIT 1),
                   0
               ) AS messengerCounter
        FROM Employee
        WHERE isActive = 1
        LIMIT 1
        """
    )
    fun selectActivePojoFlow(messengerType: String): Flow<ActiveEmployeePojo?>

    @Query("SELECT * FROM employee WHERE employeeId = :employeeId LIMIT 1")
    fun selectFlow(employeeId: String): Flow<EmployeeEntity?>

    @Transaction
    @Query("SELECT * FROM employee WHERE employeeId = :employeeId LIMIT 1")
    fun selectPojoFlow(employeeId: String): Flow<EmployeePojo?>

    @Query("SELECT * FROM employee WHERE employeeId = :employeeId LIMIT 1")
    suspend fun select(employeeId: String): EmployeeEntity?

    @Query("SELECT * FROM employee WHERE employeeId = :employeeId LIMIT 1")
    suspend fun selectNotNull(employeeId: String): EmployeeEntity

    @Query("SELECT * FROM employee ORDER BY position ASC")
    suspend fun selectAll(): List<EmployeeEntity>

    @Query("UPDATE employee SET isActive = CASE WHEN employeeId = :employeeId THEN 1 ELSE 0 END")
    suspend fun setActive(employeeId: String)

    @Upsert
    suspend fun upsert(entities: List<EmployeeEntity>)

    @Upsert
    suspend fun upsert(entity: EmployeeEntity)

    @Query("DELETE FROM employee WHERE employeeId NOT IN (:ids)")
    suspend fun deleteMissing(ids: List<String>)

    @Query("DELETE FROM employee")
    suspend fun delete()
}
