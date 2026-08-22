package ru.mercury.vpclient.features.details_cart_added_sheet.model

import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductEntity
import ru.mercury.vpclient.shared.mvi.Model

data class DetailsCartAddedModel(
    val productEntity: ProductEntity
): Model
