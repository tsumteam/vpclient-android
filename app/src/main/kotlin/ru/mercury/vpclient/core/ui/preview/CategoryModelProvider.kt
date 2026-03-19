package ru.mercury.vpclient.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.features.main.tabs.catalog.stack.category.model.CategoryModel

class CategoryModelProvider: PreviewParameterProvider<CategoryModel> {
    private val catalogCategoryEntity = CatalogCategoryEntityProvider().values.first()
    private val categoryPojo = SubcategoryPojoProvider().values.first()

    override val values: Sequence<CategoryModel> = sequenceOf(
        CategoryModel(
            entity = catalogCategoryEntity,
            pojos = listOf(categoryPojo)
        ),
        CategoryModel()
    )
}
