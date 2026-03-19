package ru.mercury.vpclient.core.ktx

import ru.mercury.vpclient.core.entity.SortType
import ru.mercury.vpclient.core.entity.SortType.ArrivalDateDescending
import ru.mercury.vpclient.core.entity.SortType.OurChoice
import ru.mercury.vpclient.core.entity.SortType.PriceAscending
import ru.mercury.vpclient.core.entity.SortType.PriceDescending
import ru.mercury.vpclient.core.network.request.FilteredProductsRequest

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
