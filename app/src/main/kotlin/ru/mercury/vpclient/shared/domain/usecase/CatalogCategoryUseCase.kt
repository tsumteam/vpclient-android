@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.features.filter.navigation.FilterRoute
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.BrandEntity
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogCategoryDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogCategoryEntity
import javax.inject.Inject

class CatalogCategoryUseCase @Inject constructor(
    private val catalogCategoryDao: CatalogCategoryDao,
    dispatchers: SharedDispatchers
): UseCase<Int, CatalogCategoryEntity?>(dispatchers.io) {

    override suspend fun execute(categoryId: Int): CatalogCategoryEntity? {
        return catalogCategoryDao.select(categoryId)
    }

    fun CatalogCategoryEntity.filterRoute(brandEntity: BrandEntity?): FilterRoute {
        return when {
            parentId == null -> {
                FilterRoute(
                    categoryId = id,
                    titleCategoryId = id,
                    subtitleCategoryId = id,
                    brandEntity = brandEntity
                )
            }
            level == CatalogCategoryEntity.LEVEL_TOP -> {
                FilterRoute(
                    categoryId = id,
                    titleCategoryId = rootId,
                    subtitleCategoryId = id,
                    brandEntity = brandEntity
                )
            }
            level == CatalogCategoryEntity.LEVEL_BOTTOM -> {
                FilterRoute(
                    categoryId = id,
                    titleCategoryId = parentId,
                    subtitleCategoryId = id,
                    brandEntity = brandEntity
                )
            }
            else -> {
                FilterRoute(
                    categoryId = id,
                    titleCategoryId = id,
                    subtitleCategoryId = parentId,
                    brandEntity = brandEntity
                )
            }
        }
    }
}
