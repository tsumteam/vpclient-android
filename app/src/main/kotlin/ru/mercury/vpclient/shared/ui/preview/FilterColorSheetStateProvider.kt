package ru.mercury.vpclient.shared.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.shared.persistence.database.entity.FilterValuesEntity
import ru.mercury.vpclient.shared.persistence.database.entity.FilterValuesQuantityEntity
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_color.model.FilterColorSheetState

class FilterColorSheetStateProvider: PreviewParameterProvider<FilterColorSheetState> {
    override val values: Sequence<FilterColorSheetState> = sequenceOf(
        FilterColorSheetState(
            entity = FilterValuesEntity(
                chipId = "color",
                title = "Цвет",
                valueIds = listOf("color_7", "color_13", "color_9", "color_3", "color_20", "color_10"),
                valueLabels = listOf("Бордовый", "Белый", "Черный", "Голубой", "Желтый", "Зеленый"),
                valueLabelPhotoUrls = listOf(
                    "https://st.vip-platinum.ru/catalog/color/62.png",
                    "https://st.vip-platinum.ru/catalog/color/68.png",
                    "https://st.vip-platinum.ru/catalog/color/64.png",
                    "https://st.vip-platinum.ru/catalog/color/58.png",
                    "https://st.vip-platinum.ru/catalog/color/75.png",
                    "https://st.vip-platinum.ru/catalog/color/65.png"
                )
            ),
            selectedIds = setOf("color_7", "color_9"),
            quantityEntity = FilterValuesQuantityEntity(
                chipId = "color",
                quantity = 128
            ),
            isProductsQuantityLoading = false,
            isLoading = false
        ),
        FilterColorSheetState(
            entity = FilterValuesEntity(
                chipId = "color",
                title = "Цвет",
                valueIds = emptyList(),
                valueLabels = emptyList(),
                valueLabelPhotoUrls = emptyList()
            ),
            selectedIds = emptySet(),
            quantityEntity = FilterValuesQuantityEntity.Empty,
            isProductsQuantityLoading = false,
            isLoading = true
        )
    )
}
