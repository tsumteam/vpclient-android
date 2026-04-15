package ru.mercury.vpclient.shared.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.shared.data.entity.SortType

class SortTypeProvider: PreviewParameterProvider<SortType> {
    override val values: Sequence<SortType> = sequenceOf(
        SortType.OurChoice,
        SortType.PriceAscending,
        SortType.PriceDescending,
        SortType.ArrivalDateDescending
    )
}
