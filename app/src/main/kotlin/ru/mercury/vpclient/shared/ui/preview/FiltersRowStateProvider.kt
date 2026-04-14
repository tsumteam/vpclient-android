package ru.mercury.vpclient.shared.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.shared.entity.FilterChip
import ru.mercury.vpclient.shared.entity.FilterRibbonData
import ru.mercury.vpclient.shared.entity.FiltersRowState

class FiltersRowStateProvider: PreviewParameterProvider<FiltersRowState> {

    private val filterRibbonData = FilterRibbonData(
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

    override val values: Sequence<FiltersRowState> = sequenceOf(
        FiltersRowState(),
        FiltersRowState(
            filterRibbonData = filterRibbonData
        ),
        FiltersRowState(
            filterRibbonData = filterRibbonData,
            sortSelected = true
        ),
        FiltersRowState(
            filterRibbonData = filterRibbonData,
            selectedFilterValueChips = listOf(
                FilterChip(
                    id = "brand_nike",
                    label = "Nike"
                )
            )
        ),
        FiltersRowState(
            filterRibbonData = filterRibbonData,
            sortSelected = true,
            selectedFilterValueChips = listOf(
                FilterChip(
                    id = "brand_nike",
                    label = "Nike"
                ),
                FilterChip(
                    id = "season_summer",
                    label = "Лето"
                )
            )
        )
    )
}
