package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.entity.SortType
import ru.mercury.vpclient.shared.data.entity.SortType.ArrivalDateDescending
import ru.mercury.vpclient.shared.data.entity.SortType.OurChoice
import ru.mercury.vpclient.shared.data.entity.SortType.PriceAscending
import ru.mercury.vpclient.shared.data.entity.SortType.PriceDescending
import ru.mercury.vpclient.shared.data.network.request.FilteredProductsRequest

val SortType.isResetButtonVisible: Boolean
    get() = this != OurChoice

val SortType.isSortChipSelected: Boolean
    get() = this != OurChoice

val SortType.requestValue: String
    get() = when (this) {
        ArrivalDateDescending -> FilteredProductsRequest.ARRIVAL_DATE_DESCENDING
        OurChoice -> FilteredProductsRequest.OUR_CHOICE
        PriceAscending -> FilteredProductsRequest.PRICE_ASCENDING
        PriceDescending -> FilteredProductsRequest.PRICE_DESCENDING
    }
