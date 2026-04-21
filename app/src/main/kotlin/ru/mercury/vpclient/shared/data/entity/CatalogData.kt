package ru.mercury.vpclient.shared.data.entity

import ru.mercury.vpclient.shared.data.CatalogPagerPage

data class CatalogData(
    val tabs: List<CatalogTabData> = emptyList(),
    val pages: List<CatalogPagerPage> = emptyList()
)
