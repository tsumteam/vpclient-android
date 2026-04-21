package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.entity.BrandFilterValue
import ru.mercury.vpclient.shared.data.entity.ColorFilterValue
import ru.mercury.vpclient.shared.data.entity.FilterChip
import ru.mercury.vpclient.shared.data.entity.SizeFilterValue
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesEntity

val FilterValuesEntity.isEmpty: Boolean
    get() = this == FilterValuesEntity.Empty

// fixme

val FilterValuesEntity.values: List<FilterChip>
    get() = items.map { item -> FilterChip(id = item.id, label = item.label) }

val FilterValuesEntity.colorValues: List<ColorFilterValue>
    get() = items.mapNotNull { item ->
        val valueLabel = item.label
        if (valueLabel.isBlank()) {
            null
        } else {
            ColorFilterValue(
                id = item.id,
                label = valueLabel,
                imageUrl = item.labelPhotoUrl.ifBlank { null }
            )
        }
    }

val FilterValuesEntity.sizeValues: List<SizeFilterValue>
    get() = items.mapNotNull { item ->
        val valueLabel = item.label
        if (valueLabel.isBlank()) {
            null
        } else {
            SizeFilterValue(
                id = item.id,
                labelRu = valueLabel,
                labelItalian = item.labelItalian.ifBlank { null },
                labelFrench = item.labelFrench.ifBlank { null },
                labelInternational = item.labelInternational.ifBlank { null }
            )
        }
    }

val FilterValuesEntity.brandValues: List<BrandFilterValue>
    get() = items.mapNotNull { item ->
        val valueLabel = item.label
        if (valueLabel.isBlank()) {
            null
        } else {
            BrandFilterValue(
                id = item.id,
                label = valueLabel,
                labelPhotoUrl = item.labelPhotoUrl.ifBlank { null },
                isFavorite = item.isFavorite,
                isTopBrand = item.isTopBrand
            )
        }
    }
