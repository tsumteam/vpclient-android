package ru.mercury.vpclient.features.details_wear_with_sheet.model

import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity

data class DetailsWearWithSheetModel(
    val products: List<CatalogFilterProductsEntity>,
    val basketProductIds: Set<String>,
    val basketProductKeys: Set<String>
) {
    fun isProductInBasket(entity: CatalogFilterProductsEntity): Boolean {
        return entity.id in basketProductIds ||
            "${entity.itemId}:${entity.colorId}" in basketProductKeys
    }
}
