package ru.mercury.vpclient.features.main.tabs.catalog.stack.category.model

import ru.mercury.vpclient.shared.mvi.Model
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogCategoryEntity
import ru.mercury.vpclient.shared.data.persistence.database.pojo.SubcategoryPojo

data class CategoryModel(
    val entity: CatalogCategoryEntity = CatalogCategoryEntity.Empty,
    val pojos: List<SubcategoryPojo> = emptyList()
): Model
