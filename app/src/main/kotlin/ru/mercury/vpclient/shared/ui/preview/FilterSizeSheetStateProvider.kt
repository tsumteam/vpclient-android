package ru.mercury.vpclient.shared.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.shared.persistence.database.entity.FilterValuesEntity
import ru.mercury.vpclient.shared.persistence.database.entity.FilterValuesQuantityEntity
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_size.model.FilterSizeSheetState

class FilterSizeSheetStateProvider: PreviewParameterProvider<FilterSizeSheetState> {
    override val values: Sequence<FilterSizeSheetState> = sequenceOf(
        FilterSizeSheetState(
            entity = FilterValuesEntity(
                chipId = "size",
                title = "Размер",
                valueIds = listOf("size_10986", "size_11711", "size_10031", "size_11139"),
                valueLabels = listOf("RU 36", "RU 38", "RU 40", "RU 42"),
                valueLabelsItalian = listOf("IT 34 | RU 36", "IT 36 | RU 38", "IT 38 | RU 40", "IT 40 | RU 42"),
                valueLabelsFrench = listOf("FR 30 | RU 36", "FR 32 | RU 38", "FR 34 | RU 40", "FR 36 | RU 42"),
                valueLabelsInternational = listOf("XXXS | RU 36", "XXXS | RU 38", "XXS | RU 40", "XS | RU 42")
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
                title = "Размер",
                valueIds = emptyList(),
                valueLabels = emptyList()
            ),
            selectedIds = emptySet(),
            quantityEntity = FilterValuesQuantityEntity.Empty,
            isProductsQuantityLoading = false,
            isLoading = true
        )
    )
}
