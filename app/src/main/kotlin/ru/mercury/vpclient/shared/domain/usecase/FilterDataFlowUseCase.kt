@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.FilterData
import ru.mercury.vpclient.shared.data.entity.FilterRequestData
import ru.mercury.vpclient.shared.data.entity.FilterRibbonData
import ru.mercury.vpclient.shared.data.entity.FilterTitleEntity
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogCategoryDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogFilterDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogFilterProductsQuantityDao
import ru.mercury.vpclient.shared.domain.mapper.orEmpty
import ru.mercury.vpclient.shared.domain.mapper.toFilterRibbonData
import ru.mercury.vpclient.shared.domain.mapper.toFilterValuesPickers
import javax.inject.Inject

class FilterDataFlowUseCase @Inject constructor(
    private val catalogCategoryDao: CatalogCategoryDao,
    private val catalogFilterDao: CatalogFilterDao,
    private val catalogFilterProductsQuantityDao: CatalogFilterProductsQuantityDao,
    dispatchers: SharedDispatchers
): FlowUseCase<FilterRequestData, FilterData>(dispatchers.io) {

    override fun execute(data: FilterRequestData): Flow<FilterData> {
        val categoryId = data.categoryId
        val titleCategoryId = data.titleCategoryId
        val subtitleCategoryId = data.subtitleCategoryId
        val filterTitleFlow = combine(
            catalogCategoryDao.selectFlow(titleCategoryId),
            catalogCategoryDao.selectFlow(subtitleCategoryId)
        ) { titleCatalogCategoryEntity, subtitleCatalogCategoryEntity ->
            FilterTitleEntity(titleCatalogCategoryEntity, subtitleCatalogCategoryEntity)
        }
        val filterRibbonDataFlow = catalogFilterDao.selectFlow(
            categoryId,
            titleCategoryId
        ).map { catalogFilterEntity ->
            when {
                catalogFilterEntity == null -> FilterRibbonData.Empty to emptyList()
                else -> catalogFilterEntity.toFilterRibbonData() to catalogFilterEntity.toFilterValuesPickers()
            }
        }
        val productsQuantityFlow = catalogFilterProductsQuantityDao.selectFlow(
            categoryId,
            titleCategoryId
        ).map { entity -> entity.orEmpty }

        return combine(
            filterTitleFlow,
            filterRibbonDataFlow,
            productsQuantityFlow
        ) { filterTitle, (filterRibbonData, filterValuesPickers), quantityEntity ->
            FilterData(
                filterTitleEntity = filterTitle,
                filterRibbonData = filterRibbonData,
                quantityEntity = quantityEntity,
                filterValuesEntities = filterValuesPickers
            )
        }
    }
}
