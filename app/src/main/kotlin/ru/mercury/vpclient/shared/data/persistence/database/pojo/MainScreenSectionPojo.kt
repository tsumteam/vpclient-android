package ru.mercury.vpclient.shared.data.persistence.database.pojo

import androidx.room.Embedded
import androidx.room.Relation
import ru.mercury.vpclient.shared.data.persistence.database.entity.MainScreenSectionEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.MainScreenSectionItemEntity

data class MainScreenSectionPojo(
    @Embedded val entity: MainScreenSectionEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "sectionId",
        entity = MainScreenSectionItemEntity::class
    ) val items: List<MainScreenSectionItemEntity>
)
