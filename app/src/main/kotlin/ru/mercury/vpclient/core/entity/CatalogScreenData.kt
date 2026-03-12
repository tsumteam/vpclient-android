package ru.mercury.vpclient.core.entity

import ru.mercury.vpclient.core.CatalogPagerPage

data class CatalogScreenData(
    val tabs: List<String> = emptyList(),
    val pages: List<CatalogPagerPage> = emptyList()
)
