package ru.mercury.vpclient.features.details_message_sheet.model

import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductEntity
import ru.mercury.vpclient.shared.mvi.Model

data class DetailsMessageModel(
    val productEntity: ProductEntity,
    val commentText: String = ""
): Model
