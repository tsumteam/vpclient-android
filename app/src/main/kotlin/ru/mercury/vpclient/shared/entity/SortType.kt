package ru.mercury.vpclient.shared.entity

sealed interface SortType {
    data object OurChoice: SortType
    data object PriceAscending: SortType
    data object PriceDescending: SortType
    data object ArrivalDateDescending: SortType
}
