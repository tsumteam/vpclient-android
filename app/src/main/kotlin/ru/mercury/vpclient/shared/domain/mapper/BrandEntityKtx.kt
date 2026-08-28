package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.entity.BrandEntity

val BrandEntity.isLogoVisible: Boolean
    get() = !urlBrandLogo.isNullOrEmpty()

val BrandEntity.isTextVisible: Boolean
    get() = brand.isNotEmpty()
