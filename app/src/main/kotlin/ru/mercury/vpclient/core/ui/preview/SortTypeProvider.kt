package ru.mercury.vpclient.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.core.entity.SortType

class SortTypeProvider: PreviewParameterProvider<SortType> {
    override val values: Sequence<SortType> = sequenceOf(
        SortType.OurChoice,
        SortType.PriceAscending,
        SortType.PriceDescending,
        SortType.ArrivalDateDescending
    )
}
