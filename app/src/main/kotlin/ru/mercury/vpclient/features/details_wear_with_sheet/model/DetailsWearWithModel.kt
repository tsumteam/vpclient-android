package ru.mercury.vpclient.features.details_wear_with_sheet.model

import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.mvi.Model

data class DetailsWearWithModel(
    val products: List<CatalogFilterProductsEntity>,
    val basketProductIds: Set<String>,
    val basketProductKeys: Set<String>
): Model {

    fun isProductInBasket(entity: CatalogFilterProductsEntity): Boolean {
        return "${entity.itemId}:${entity.colorId}:" in basketProductKeys ||
            "${entity.itemId}:${entity.colorId}:NS" in basketProductKeys
    }
}
