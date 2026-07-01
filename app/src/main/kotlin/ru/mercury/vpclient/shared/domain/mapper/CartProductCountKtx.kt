package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.entity.CartProduct

val CartProduct.itemsCount: Int
    get() = quantity * sizeCount

val List<CartProduct>.itemsCount: Int
    get() = sumOf { product -> product.itemsCount }
