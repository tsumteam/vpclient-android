package ru.mercury.vpclient.shared.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.shared.entity.FilterChip
import ru.mercury.vpclient.shared.entity.FilterData
import ru.mercury.vpclient.shared.entity.FilterRibbonData
import ru.mercury.vpclient.shared.entity.FilterTitleEntity
import ru.mercury.vpclient.shared.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.persistence.database.entity.FilterValuesEntity
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter.model.FilterModel

class FilterModelProvider: PreviewParameterProvider<Pair<FilterModel, List<CatalogFilterProductsEntity>>> {

    private val titleCatalogCategoryEntity = CatalogCategoryEntityProvider().values.first()
    private val subtitleCatalogCategoryEntity = CatalogCategoryEntityProvider2().values.first()
    private val quantityEntity = FilterProductsQuantityEntityProvider().values.last()
    private val productsEntities = CatalogFilterProductsEntitiesProvider().values.first()

    override val values: Sequence<Pair<FilterModel, List<CatalogFilterProductsEntity>>> = sequenceOf(
        FilterModel() to productsEntities,
        FilterModel(
            filterData = FilterData(
                filterTitleEntity = FilterTitleEntity(
                    titleCatalogCategoryEntity = titleCatalogCategoryEntity,
                    subtitleCatalogCategoryEntity = subtitleCatalogCategoryEntity
                ),
                filterRibbonData = FilterRibbonData(
                    topFilterChips = listOf(
                        FilterChip(
                            id = "brand",
                            label = "Бренд"
                        ),
                        FilterChip(
                            id = "size",
                            label = "Размер"
                        ),
                        FilterChip(
                            id = "color",
                            label = "Цвет"
                        )
                    ),
                    topFilterValueChips = listOf(
                        FilterChip(
                            id = "brand_nike",
                            label = "Nike"
                        ),
                        FilterChip(
                            id = "brand_adidas",
                            label = "Adidas"
                        )
                    ),
                    bottomFilterChips = listOf(
                        FilterChip(
                            id = "materialAttribute",
                            label = "Материал"
                        ),
                        FilterChip(
                            id = "attribute_length",
                            label = "Длина"
                        )
                    )
                ),
                quantityEntity = quantityEntity,
                filterValuesEntities = listOf(
                    FilterValuesEntity(
                        chipId = "attribute_length",
                        title = "Длина",
                        valueIds = listOf("attribute_length_mini", "attribute_length_midi", "attribute_length_maxi"),
                        valueLabels = listOf("Мини", "Миди", "Макси")
                    )
                )
            )
        ) to productsEntities
    )
}
