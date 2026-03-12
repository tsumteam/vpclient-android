package ru.mercury.vpclient.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.features.main.tabs.catalog.stack.subcategory.model.SubcategoryModel

class SubcategoryModelProvider: PreviewParameterProvider<SubcategoryModel> {
    private val catalogCategoryEntity = CatalogCategoryEntityProvider().values.first()
    private val subcategoryPojo = SubcategoryPojoProvider().values.first()

    override val values: Sequence<SubcategoryModel> = sequenceOf(
        SubcategoryModel(
            entity = catalogCategoryEntity,
            pojos = listOf(subcategoryPojo)
        ),
        SubcategoryModel()
    )
}
