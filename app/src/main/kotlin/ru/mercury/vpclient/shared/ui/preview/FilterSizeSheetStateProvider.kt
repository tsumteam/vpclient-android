package ru.mercury.vpclient.shared.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValueItemEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesQuantityEntity
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_size.model.FilterSizeSheetState

class FilterSizeSheetStateProvider: PreviewParameterProvider<FilterSizeSheetState> {
    override val values: Sequence<FilterSizeSheetState> = sequenceOf(
        FilterSizeSheetState(
            entity = FilterValuesEntity(
                chipId = "size",
                title = "Размер",
                items = listOf(
                    FilterValueItemEntity(
                        id = "size_10986",
                        label = "RU 36",
                        labelItalian = "IT 34 | RU 36",
                        labelFrench = "FR 30 | RU 36",
                        labelInternational = "XXXS | RU 36"
                    ),
                    FilterValueItemEntity(
                        id = "size_11711",
                        label = "RU 38",
                        labelItalian = "IT 36 | RU 38",
                        labelFrench = "FR 32 | RU 38",
                        labelInternational = "XXXS | RU 38"
                    ),
                    FilterValueItemEntity(
                        id = "size_10031",
                        label = "RU 40",
                        labelItalian = "IT 38 | RU 40",
                        labelFrench = "FR 34 | RU 40",
                        labelInternational = "XXS | RU 40"
                    ),
                    FilterValueItemEntity(
                        id = "size_11139",
                        label = "RU 42",
                        labelItalian = "IT 40 | RU 42",
                        labelFrench = "FR 36 | RU 42",
                        labelInternational = "XS | RU 42"
                    )
                )
            ),
            selectedIds = setOf("size_10986", "size_10031"),
            quantityEntity = FilterValuesQuantityEntity(
                chipId = "size",
                quantity = 128
            ),
            isProductsQuantityLoading = false,
            isLoading = false
        ),
        FilterSizeSheetState(
            entity = FilterValuesEntity(
                chipId = "size",
                title = "Размер"
            ),
            selectedIds = emptySet(),
            quantityEntity = FilterValuesQuantityEntity.Empty,
            isProductsQuantityLoading = false,
            isLoading = true
        )
    )
}
