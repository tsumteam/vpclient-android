package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.network.response.CompilationResponse
import ru.mercury.vpclient.shared.data.persistence.database.entity.CompilationEntity

fun CompilationResponse.entity(
    position: Int
): CompilationEntity {
    return CompilationEntity(
        id = id.orEmpty,
        position = position,
        collageUrl = collageUrl.orEmpty(),
        photoUrl = photoUrl.orEmpty(),
        name = name.orEmpty(),
        description = description.orEmpty(),
        createDate = createDate.orEmpty(),
        looksQty = looksQty.orEmpty,
        lookProductsQty = lookProductsQty.orEmpty,
        badge = badge.orEmpty,
        isStatsAvailable = isStatsAvailable.orEmpty
    )
}
