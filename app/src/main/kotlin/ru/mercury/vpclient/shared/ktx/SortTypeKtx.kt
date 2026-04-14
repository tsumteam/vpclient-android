package ru.mercury.vpclient.shared.ktx

import ru.mercury.vpclient.shared.entity.SortType
import ru.mercury.vpclient.shared.entity.SortType.ArrivalDateDescending
import ru.mercury.vpclient.shared.entity.SortType.OurChoice
import ru.mercury.vpclient.shared.entity.SortType.PriceAscending
import ru.mercury.vpclient.shared.entity.SortType.PriceDescending
import ru.mercury.vpclient.shared.network.request.FilteredProductsRequest

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
