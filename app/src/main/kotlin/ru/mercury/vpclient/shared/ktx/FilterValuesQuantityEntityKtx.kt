package ru.mercury.vpclient.shared.ktx

import ru.mercury.vpclient.shared.persistence.database.entity.FilterValuesQuantityEntity

val FilterValuesQuantityEntity.isEmpty: Boolean
    get() = this == FilterValuesQuantityEntity.Empty

val FilterValuesQuantityEntity.requireQuantity: Int
    get() = quantity.orEmpty

val FilterValuesQuantityEntity.quantityWithThousandsSeparator: String
    get() = requireQuantity.thousandsSeparator
