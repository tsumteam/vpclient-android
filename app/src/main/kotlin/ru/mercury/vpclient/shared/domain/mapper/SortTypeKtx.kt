package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.entity.SortType
import ru.mercury.vpclient.shared.data.entity.SortType.ArrivalDateDescending
import ru.mercury.vpclient.shared.data.entity.SortType.OurChoice
import ru.mercury.vpclient.shared.data.entity.SortType.PriceAscending
import ru.mercury.vpclient.shared.data.entity.SortType.PriceDescending
import ru.mercury.vpclient.shared.data.network.type.FilteredProductsSortType

val SortType.isResetButtonVisible: Boolean
    get() = this != OurChoice

val SortType.isSortChipSelected: Boolean
    get() = this != OurChoice

val SortType.requestValue: FilteredProductsSortType
    get() = when (this) {
        ArrivalDateDescending -> FilteredProductsSortType.ARRIVAL_DATE_DESCENDING
        OurChoice -> FilteredProductsSortType.OUR_CHOICE
        PriceAscending -> FilteredProductsSortType.PRICE_ASCENDING
        PriceDescending -> FilteredProductsSortType.PRICE_DESCENDING
    }
