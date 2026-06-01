package ru.mercury.vpclient.features.details_wear_with_sheet.intent

import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface DetailsWearWithSheetIntent: Intent {
    data class ProductClick(val id: String): DetailsWearWithSheetIntent
    data class ProductBasketClick(val product: CatalogFilterProductsEntity): DetailsWearWithSheetIntent
    data object DismissRequest: DetailsWearWithSheetIntent
}
