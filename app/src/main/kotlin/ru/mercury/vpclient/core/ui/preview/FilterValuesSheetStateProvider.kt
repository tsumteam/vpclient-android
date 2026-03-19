package ru.mercury.vpclient.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.core.persistence.database.entity.FilterValuesEntity
import ru.mercury.vpclient.core.persistence.database.entity.FilterValuesQuantityEntity
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_values.model.FilterValuesSheetState

class FilterValuesSheetStateProvider: PreviewParameterProvider<FilterValuesSheetState> {
    override val values: Sequence<FilterValuesSheetState> = sequenceOf(
        FilterValuesSheetState(
            entity = FilterValuesEntity(
                chipId = "attribute_length",
                title = "ДЛИНА",
                valueIds = listOf("attribute_length_mini", "attribute_length_midi", "attribute_length_maxi", "attribute_length_ankle"),
                valueLabels = listOf("Мини", "Миди", "Макси", "До щиколотки")
            ),
            selectedIds = setOf("attribute_length_midi", "attribute_length_maxi"),
            quantityEntity = FilterValuesQuantityEntity(
                chipId = "attribute_length",
                quantity = 128
            ),
            isProductsQuantityLoading = false,
            isLoading = false
        ),
        FilterValuesSheetState(
            entity = FilterValuesEntity(
                chipId = "attribute_length",
                title = "ДЛИНА",
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
