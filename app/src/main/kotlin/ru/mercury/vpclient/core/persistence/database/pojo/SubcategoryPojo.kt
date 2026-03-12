package ru.mercury.vpclient.core.persistence.database.pojo

import androidx.room.Embedded
import androidx.room.Relation
import ru.mercury.vpclient.core.persistence.database.entity.CatalogCategoryEntity

data class SubcategoryPojo(
    @Embedded val entity: CatalogCategoryEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "parentId",
        entity = CatalogCategoryEntity::class
    ) val children: List<CatalogCategoryEntity>
)
