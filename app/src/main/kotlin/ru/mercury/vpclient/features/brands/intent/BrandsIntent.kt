package ru.mercury.vpclient.features.brands.intent

import ru.mercury.vpclient.shared.data.entity.TabType
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogBrandEntity
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface BrandsIntent: Intent {
    data object CollectBrands: BrandsIntent
    data object CollectCartCount: BrandsIntent
    data object CollectFittingCount: BrandsIntent
    data object CollectActiveEmployee: BrandsIntent
    data object LoadBrands: BrandsIntent
    data object FavoriteBrandsClick: BrandsIntent
    data object SearchClick: BrandsIntent
    data object CartClick: BrandsIntent
    data object FittingClick: BrandsIntent
    data object MessengerClick: BrandsIntent
    data class SelectTab(val tab: TabType): BrandsIntent
    data class BrandClick(val entity: CatalogBrandEntity): BrandsIntent
    data class FavoriteClick(val entity: CatalogBrandEntity): BrandsIntent
}
