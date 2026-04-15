package ru.mercury.vpclient.features.main.tabs.catalog.stack.catalog.model

import ru.mercury.vpclient.shared.data.entity.CatalogData
import ru.mercury.vpclient.shared.mvi.Model

data class CatalogModel(
    val catalogData: CatalogData = CatalogData()
): Model {

    val isLoading: Boolean
        get() = catalogData.pages.isEmpty()
}
