package ru.mercury.vpclient.shared.data.persistence.database.entity

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class ProductButtonEntity(
    val title: String,
    val catalogLink: JsonObject? = null
)
