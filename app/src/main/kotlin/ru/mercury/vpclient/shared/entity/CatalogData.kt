package ru.mercury.vpclient.shared.entity

import ru.mercury.vpclient.shared.CatalogPagerPage

data class CatalogData(
    val tabs: List<String> = emptyList(),
    val pages: List<CatalogPagerPage> = emptyList()
)
