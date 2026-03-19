package ru.mercury.vpclient.core.entity

data class BrandFilterValue(
    val id: String,
    val label: String,
    val labelPhotoUrl: String?,
    val isFavorite: Boolean,
    val isTopBrand: Boolean
)
