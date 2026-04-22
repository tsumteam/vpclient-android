package ru.mercury.vpclient.shared.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class BrandEntity(
    val brand: String,
    val urlBrandLogo: String?
) {
    companion object {
        val Empty = BrandEntity(
            brand = "",
            urlBrandLogo = null
        )
    }
}
