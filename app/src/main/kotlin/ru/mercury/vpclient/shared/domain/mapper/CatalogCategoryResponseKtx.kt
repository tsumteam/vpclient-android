package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.network.response.CatalogCategoryResponse
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogCategoryEntity

fun CatalogCategoryResponse.basicEntity(
    position: Int
): CatalogCategoryEntity {
    return CatalogCategoryEntity(
        id = id.orEmpty,
        parentId = null,
        rootId = id.orEmpty,
        level = CatalogCategoryEntity.LEVEL_BASIC,
        name = name.orEmpty(),
        photoUrl = photoUrl.orEmpty(),
        categoryType = categoryType.orEmpty(),
        sortSettingId = sortSettingId.orEmpty,
        position = position
    )
}

fun CatalogCategoryResponse.topEntity(
    parentId: Int,
    rootId: Int,
    position: Int
): CatalogCategoryEntity {
    return CatalogCategoryEntity(
        id = id.orEmpty,
        parentId = parentId,
        rootId = rootId,
        level = CatalogCategoryEntity.LEVEL_TOP,
        name = name.orEmpty(),
        photoUrl = photoUrl.orEmpty(),
        categoryType = categoryType.orEmpty(),
        sortSettingId = sortSettingId.orEmpty,
        position = position
    )
}

fun CatalogCategoryResponse.bottomEntity(
    parentId: Int,
    rootId: Int,
    position: Int
): CatalogCategoryEntity {
    return childEntity(
        parentId = parentId,
        rootId = rootId,
        level = CatalogCategoryEntity.LEVEL_BOTTOM,
        position = position
    )
}

fun CatalogCategoryResponse.childEntity(
    parentId: Int,
    rootId: Int,
    level: Int,
    position: Int
): CatalogCategoryEntity {
    return CatalogCategoryEntity(
        id = id.orEmpty,
        parentId = parentId,
        rootId = rootId,
        level = level,
        name = name.orEmpty(),
        photoUrl = photoUrl.orEmpty(),
        categoryType = categoryType.orEmpty(),
        sortSettingId = sortSettingId.orEmpty,
        position = position
    )
}
