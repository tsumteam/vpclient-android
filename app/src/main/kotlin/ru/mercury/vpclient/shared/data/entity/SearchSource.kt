package ru.mercury.vpclient.shared.data.entity

import kotlinx.serialization.Serializable

@Serializable
enum class SearchSource {
    CATALOG,
    CART,
    FITTING,
    PALETTE
}
