package ru.mercury.vpclient.features.details_wear_with_sheet.intent

import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface DetailsWearWithIntent: Intent {
    data object DismissClick: DetailsWearWithIntent
    data class ProductClick(val id: String): DetailsWearWithIntent
    data class ProductBasketClick(val product: CatalogFilterProductsEntity): DetailsWearWithIntent
}
