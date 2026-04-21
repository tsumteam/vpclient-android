package ru.mercury.vpclient.shared.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValueItemEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesQuantityEntity
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_color.model.FilterColorSheetState

class FilterColorSheetStateProvider: PreviewParameterProvider<FilterColorSheetState> {
    override val values: Sequence<FilterColorSheetState> = sequenceOf(
        FilterColorSheetState(
            entity = FilterValuesEntity(
                chipId = "color",
                title = "Цвет",
                items = listOf(
                    FilterValueItemEntity(id = "color_7", label = "Бордовый", labelPhotoUrl = "https://st.vip-platinum.ru/catalog/color/62.png"),
                    FilterValueItemEntity(id = "color_13", label = "Белый", labelPhotoUrl = "https://st.vip-platinum.ru/catalog/color/68.png"),
                    FilterValueItemEntity(id = "color_9", label = "Черный", labelPhotoUrl = "https://st.vip-platinum.ru/catalog/color/64.png"),
                    FilterValueItemEntity(id = "color_3", label = "Голубой", labelPhotoUrl = "https://st.vip-platinum.ru/catalog/color/58.png"),
                    FilterValueItemEntity(id = "color_20", label = "Желтый", labelPhotoUrl = "https://st.vip-platinum.ru/catalog/color/75.png"),
                    FilterValueItemEntity(id = "color_10", label = "Зеленый", labelPhotoUrl = "https://st.vip-platinum.ru/catalog/color/65.png")
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
                title = "Цвет"
            ),
            selectedIds = emptySet(),
            quantityEntity = FilterValuesQuantityEntity.Empty,
            isProductsQuantityLoading = false,
            isLoading = true
        )
    )
}
