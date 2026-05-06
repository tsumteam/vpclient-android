package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.network.response.CartCountLineResponse

val CartCountLineResponse.itemsCount: Int
    get() {
        return (quantity ?: 1) * sizeCount
    }

private val CartCountLineResponse.sizeCount: Int
    get() {
        val sizesCount = products.orEmpty()
            .firstOrNull()
            ?.product
            ?.sizes
            .orEmpty()
            .size
        return when {
            sizesCount > 0 -> sizesCount
            else -> 1
        }
    }
