package ru.mercury.vpclient.features.filter_values_sheet.model

import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesQuantityEntity
import ru.mercury.vpclient.shared.mvi.Model

data class FilterValuesModel(
    val entity: FilterValuesEntity,
    val selectedIds: Set<String>,
    val quantityEntity: FilterValuesQuantityEntity,
    val isProductsQuantityLoading: Boolean,
    val isLoading: Boolean = false
): Model {

    val isResetButtonVisible: Boolean
        get() = selectedIds.isNotEmpty()
}
