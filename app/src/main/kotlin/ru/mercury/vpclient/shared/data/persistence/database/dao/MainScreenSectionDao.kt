package ru.mercury.vpclient.shared.data.persistence.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.data.network.type.MainScreenCategoryType
import ru.mercury.vpclient.shared.data.persistence.database.entity.MainScreenSectionEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.MainScreenSectionItemEntity
import ru.mercury.vpclient.shared.data.persistence.database.pojo.MainScreenSectionPojo

@Dao
interface MainScreenSectionDao {

    @Transaction
    @Query("SELECT * FROM MainScreenSection WHERE category = :category ORDER BY `order` ASC")
    fun selectFlow(category: MainScreenCategoryType): Flow<List<MainScreenSectionPojo>>

    @Query(
        "DELETE FROM MainScreenSectionItem WHERE sectionId IN " +
            "(SELECT id FROM MainScreenSection WHERE category = :category)"
    )
    suspend fun deleteItems(category: MainScreenCategoryType)

    @Query("DELETE FROM MainScreenSection WHERE category = :category")
    suspend fun delete(category: MainScreenCategoryType)

    @Upsert
    suspend fun upsertSections(entities: List<MainScreenSectionEntity>)

    @Upsert
    suspend fun upsertItems(entities: List<MainScreenSectionItemEntity>)
}
