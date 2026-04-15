package ru.mercury.vpclient.shared.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.shared.data.entity.FilterChip
import ru.mercury.vpclient.shared.data.entity.FilterRibbonData

class FilterRibbonDataProvider: PreviewParameterProvider<FilterRibbonData> {
    override val values: Sequence<FilterRibbonData> = sequenceOf(
        FilterRibbonData.Empty,
        FilterRibbonData(
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
                    id = "season",
                    label = "Сезон"
                )
            )
        )
    )
}
