package ru.mercury.vpclient.features.main.tabs.catalog.stack.catalog.model

import ru.mercury.vpclient.core.entity.CatalogScreenData
import ru.mercury.vpclient.core.mvi.Model

data class CatalogModel(
    val catalogScreenData: CatalogScreenData = CatalogScreenData()
): Model {

    val isLoading: Boolean
        get() = catalogScreenData.pages.isEmpty()
}
