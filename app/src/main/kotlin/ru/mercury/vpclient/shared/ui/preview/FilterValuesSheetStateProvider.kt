package ru.mercury.vpclient.shared.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValueItemEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesQuantityEntity
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_values.model.FilterValuesSheetState

class FilterValuesSheetStateProvider: PreviewParameterProvider<FilterValuesSheetState> {
    override val values: Sequence<FilterValuesSheetState> = sequenceOf(
        FilterValuesSheetState(
            entity = FilterValuesEntity(
                chipId = "attribute_length",
                title = "ДЛИНА",
                items = listOf(
                    FilterValueItemEntity(id = "attribute_length_mini", label = "Мини"),
                    FilterValueItemEntity(id = "attribute_length_midi", label = "Миди"),
                    FilterValueItemEntity(id = "attribute_length_maxi", label = "Макси"),
                    FilterValueItemEntity(id = "attribute_length_ankle", label = "До щиколотки")
                )
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
                title = "ДЛИНА"
            ),
            selectedIds = emptySet(),
            quantityEntity = FilterValuesQuantityEntity.Empty,
            isProductsQuantityLoading = false,
            isLoading = true
        )
    )
}
