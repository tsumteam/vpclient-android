package ru.mercury.vpclient.shared.data.entity

data class BrandFilterValue(
    val id: String,
    val label: String,
    val labelPhotoUrl: String?,
    val isFavorite: Boolean,
    val isTopBrand: Boolean
)
