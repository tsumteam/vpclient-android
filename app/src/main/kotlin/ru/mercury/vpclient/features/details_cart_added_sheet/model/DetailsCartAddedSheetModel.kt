package ru.mercury.vpclient.features.details_cart_added_sheet.model

import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductEntity

data class DetailsCartAddedSheetModel(
    val productEntity: ProductEntity
)
