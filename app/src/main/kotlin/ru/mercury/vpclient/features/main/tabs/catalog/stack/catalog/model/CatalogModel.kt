package ru.mercury.vpclient.features.main.tabs.catalog.stack.catalog.model

import ru.mercury.vpclient.core.entity.CatalogData
import ru.mercury.vpclient.core.mvi.Model

data class CatalogModel(
    val catalogData: CatalogData = CatalogData()
): Model {

    val isLoading: Boolean
        get() = catalogData.pages.isEmpty()
}
