package ru.mercury.vpclient.features.details_message_sheet.model

import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductEntity

data class DetailsChatModel(
    val productEntity: ProductEntity,
    val commentText: String = ""
)
